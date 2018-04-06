package com.tsystems.timetable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.timetable.dto.UpdateTimetableData;
import com.tsystems.timetable.producer.Property;
import com.tsystems.timetable.pusher.PushBean;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


@Singleton
@Startup
@DependsOn("TimetableInfoLoader")
@Log4j
public class KafkaInitializer {

    @Inject
    @Property("kafka")
    private Properties kafkaProperties;

    @Inject
    private TimetableInfoLoader timetableInfoLoader;

    @Inject
    private PushBean pushBean;

    @Inject
    private ObjectMapper objectMapper;

    private final List<Consumer<String, String>> consumers = new ArrayList<>();

    public void subscribe(String station) {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(kafkaProperties);
        kafkaConsumer.subscribe(Collections.singletonList(station));
        consumers.add(kafkaConsumer);
        new Thread(() -> {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                    records.forEach(record -> updateTimetable(station, record.value()));
                }
            } catch (IllegalStateException e) {
                log.debug("Consuming is finished", e);
            }
        }).start();
    }

    private void updateTimetable(String station, String message) {
        log.info(MessageFormat.format("Message: {0}", message));
        System.out.println(message);
        try {
            UpdateTimetableData updateTimetableData = objectMapper.readValue(message, UpdateTimetableData.class);
            timetableInfoLoader.updateStationTimetableStorage(station, updateTimetableData);
            pushBean.clockAction();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e,e);
        }
    }

    @PreDestroy
    public void destroy(){
        consumers.forEach(this::closeConsumer);
    }

    private void closeConsumer(Consumer<String, String> consumer) {
        consumer.unsubscribe();
        consumer.close();
    }
}
