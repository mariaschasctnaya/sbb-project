package com.tsystems.train.facade.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.StationStatus;
import com.tsystems.train.entity.Train;
import com.tsystems.train.facade.TrainFacade;
import com.tsystems.train.facade.converter.DtoConverter;
import com.tsystems.train.facade.data.ScheduleData;
import com.tsystems.train.facade.data.SearchTrainData;
import com.tsystems.train.facade.data.TrainData;
import com.tsystems.train.facade.data.UpdateTimetableData;
import com.tsystems.train.service.MessageService;
import com.tsystems.train.service.RouteService;
import com.tsystems.train.service.StationStatusService;
import com.tsystems.train.service.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class TrainFacadeImpl implements TrainFacade {


    @Autowired
    private TrainService trainService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private StationStatusService stationStatusService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DtoConverter<TrainData, Train> createTrainDataConverter;
    @Autowired
    private DtoConverter<Train, TrainData> trainConverter;

    @Override
    public void createTrain(TrainData trainData) {
        Train train = createTrainDataConverter.convert(trainData);
        trainService.createTrain(train);
        notify(train, false);
    }

    private void notify(Train train, boolean refresh) {
        TrainData trainData = trainConverter.convert(train);
        trainData.getStationSchedules().forEach((station, schedule) -> notify(trainData, station, schedule, refresh));
    }

    private void notify(TrainData trainData, String station, ScheduleData schedule, boolean refresh) {
        UpdateTimetableData updateTimetableData = new UpdateTimetableData();
        updateTimetableData.setArrive(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(schedule.getArrive()));
        updateTimetableData.setDeparture(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(schedule.getDeparture()));
        if (!refresh) {
            updateTimetableData.setStatus(schedule.getStatus());
            log.debug("TrainService: archiving status {}", schedule.getStatus());
        } else {
            updateTimetableData.setStatus(StationStatus.Status.CANCELED.toString());
            log.debug("TrainService: archiving status for train canceled");
        }
        updateTimetableData.setTrainNumber(trainData.getNumber());
        updateTimetableData.setRefresh(refresh);
        messageService.send(station, updateTimetableData);
    }

    @Override
    public List<TrainData> searchTrains(SearchTrainData searchTrainData) {
        return trainService.searchTrain(searchTrainData.getDeparture(), searchTrainData.getArrived(),
                searchTrainData.getStartDate(), searchTrainData.getEndDate())
                .stream()
                .filter(this::filterByStatus)
                .filter(train -> byStatusStations(train, searchTrainData))
                .map(trainConverter::convert)
                .collect(Collectors.toList());
    }

    private boolean byStatusStations(Train train, SearchTrainData searchTrainData) {
        if (train.getStationStatuses() != null) {
            StationStatus.Status arrivedStatus = getStationStatus(train, searchTrainData.getArrived());
            StationStatus.Status departureStatus = getStationStatus(train, searchTrainData.getDeparture());
            return arrivedStatus != StationStatus.Status.CANCELED && departureStatus != StationStatus.Status.CANCELED;
        }
        return true;
    }

    private StationStatus.Status getStationStatus(Train train, String station) {
        return train.getStationStatuses().stream()
                .filter(status -> status.getStation().getName().equals(station))
                .findFirst()
                .map(StationStatus::getStatus)
                .orElse(StationStatus.Status.OK);
    }

    @Override
    public List<TrainData> getTrains(String station, boolean filter) {
        Stream<Train> trains = trainService.getTrains(station).stream();
        if (filter) {
            trains = trains.filter(this::filterByStatus);
        }
        return trains.map(trainConverter::convert).collect(Collectors.toList());
    }

    private boolean filterByStatus(Train train) {
        return train.getStatus() == EntityStatus.ACTIVE;
    }


    @Override
    public void updateStatus(String trainNumber, String station, String status, Integer delayTime) {
        Train train = trainService.getTrainByNumber(trainNumber);
        train.getStationStatuses().stream()
                .filter(stationStatus -> byStation(stationStatus, station))
                .forEach(stationStatus -> updateStatus(stationStatus, status, delayTime));
        train.getStationStatuses().stream()
                .filter(stationStatus -> byStation(stationStatus, station))
                .forEach(stationStatusService::update);
    }



    private void updateStatus(StationStatus stationStatus, String status, Integer delay) {
        stationStatus.setDelay(delay);
        stationStatus.setStatus(StationStatus.Status.valueOf(status));
        UpdateTimetableData updateTimetableData = new UpdateTimetableData();
        updateTimetableData.setStatus(status);
        updateTimetableData.setTrainNumber(stationStatus.getTrain().getNumber());
        updateTimetableData.setDelayTime(delay);
        messageService.send(stationStatus.getStation().getName(), updateTimetableData);
    }

    private boolean byStation(StationStatus status, String station) {
        return status.getStation().getName().equals(station);
    }

    @Override
    public void archiveTrain(String id) {
        Train train = trainService.getTrainById(id);
        trainService.archive(train);
        notify(train, true);
    }

    @Override
    public void archiveTrainByRoute(String routeId) {
        Route route = routeService.getRouteById(routeId);
        trainService.archiveTrainByRoute(route);
        List<Train> trainsByRoute = trainService.getTrainsByRoute(route);
        trainsByRoute.forEach(train -> notify(train, true));
    }


}
