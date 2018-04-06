package com.tsystems.train.service.impl;

import com.tsystems.train.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
//MessageServiceImpl sends a message to Kafka,takes the name of the station and the message
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Async
    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
