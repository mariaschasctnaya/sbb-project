package com.tsystems.timetable;

import com.tsystems.timetable.dto.Timetable;
import com.tsystems.timetable.dto.Train;
import com.tsystems.timetable.dto.TrainTimetable;
import com.tsystems.timetable.dto.UpdateTimetableData;
import com.tsystems.timetable.producer.Property;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Startup
public class TimetableInfoLoader {

    private Map<String, Collection<TrainTimetable>> stationsTimetableStorage;

    @Inject
    @Property("train-service.url")
    private String trainServiceUrl;

    @PostConstruct
    public void init() {
        fillStationTimetableStorage();
    }

    public Map<String, Collection<TrainTimetable>> getStationsTimetableStorage(){
        return stationsTimetableStorage;
    }


    private void fillStationTimetableStorage() {
        Client client = ClientBuilder.newClient();
        List<Train> stationTrains = client.target(trainServiceUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Train>>(){});
        stationsTimetableStorage = new HashMap<>();
        stationTrains.forEach(this::toTrainTimetable);
    }

    private void toTrainTimetable(Train train) {
        train.getStationSchedules().forEach((k,v) -> putTrainTimetable(train.getNumber(), k, v));
    }

    private void putTrainTimetable(String trainNumber, String station, Timetable timetable) {
        stationsTimetableStorage.putIfAbsent(station, new HashSet<>());
        TrainTimetable trainTimetable = new TrainTimetable();
        trainTimetable.setId(trainNumber);
        trainTimetable.setArrivedTime(timetable.getArrive());
        trainTimetable.setDepartureTime(timetable.getDeparture());
        trainTimetable.setStatus(timetable.getStatus());
        stationsTimetableStorage.get(station).add(trainTimetable);
    }

    public void updateStationTimetableStorage(String station, UpdateTimetableData updateTimetableData) {
        Collection<TrainTimetable> trainTimetables = stationsTimetableStorage.get(station);
        List<TrainTimetable> trainTimetableList = trainTimetables.stream()
                .filter(trainTimetable -> Objects.equals(trainTimetable.getId(), updateTimetableData.getTrainNumber())).collect(Collectors.toList());
        if(trainTimetableList.isEmpty()) {
            createTimetable(station, updateTimetableData);
            return;
        }
        trainTimetableList.forEach(trainTimetable -> updateTrainTimetableStatus(trainTimetable, updateTimetableData));
    }

    private void createTimetable(String station, UpdateTimetableData updateTimetableData) {
        Collection<TrainTimetable> trainTimetables = stationsTimetableStorage.get(station);
        if(trainTimetables == null || trainTimetables.isEmpty()) {
            trainTimetables = new HashSet<>();
            stationsTimetableStorage.putIfAbsent(station, trainTimetables);
        }
        TrainTimetable trainTimetable = new TrainTimetable();
        trainTimetable.setId(updateTimetableData.getTrainNumber());
        trainTimetable.setArrivedTime(updateTimetableData.getArrive());
        trainTimetable.setDepartureTime(updateTimetableData.getDeparture());
        trainTimetable.setStatus(updateTimetableData.getStatus());
        stationsTimetableStorage.get(station).add(trainTimetable);
    }

    private void updateTrainTimetableStatus(TrainTimetable trainTimetable, UpdateTimetableData updateTimetableData) {
        trainTimetable.setStatus(updateTimetableData.getStatus());
        if(Objects.equals(updateTimetableData.getStatus(), "DELAYED")) {
            if(trainTimetable.getShift() != null) {
                trainTimetable.setArrivedTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        .format(LocalDateTime.parse(trainTimetable.getArrivedTime()).minusMinutes(trainTimetable.getShift())));
                trainTimetable.setDepartureTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        .format(LocalDateTime.parse(trainTimetable.getDepartureTime()).minusMinutes(trainTimetable.getShift())));
            }
            trainTimetable.setShift(updateTimetableData.getDelayTime());
        }
        if(trainTimetable.getShift() != null) {
            trainTimetable.setArrivedTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    .format(LocalDateTime.parse(trainTimetable.getArrivedTime()).plusMinutes(trainTimetable.getShift())));
            trainTimetable.setDepartureTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    .format(LocalDateTime.parse(trainTimetable.getDepartureTime()).plusMinutes(trainTimetable.getShift())));
        }
    }


}
