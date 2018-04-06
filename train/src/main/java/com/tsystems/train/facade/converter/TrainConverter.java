package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.StationStatus;
import com.tsystems.train.entity.Train;
import com.tsystems.train.facade.data.RouteData;
import com.tsystems.train.facade.data.ScheduleData;
import com.tsystems.train.facade.data.TrainData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TrainConverter implements DtoConverter<Train, TrainData> {

    @Autowired
    private DtoConverter<Route, RouteData> routeConverter;


    @Override
    public TrainData convert(Train train) {
        RouteData routeData = routeConverter.convert(train.getRoute());
        Map<String, ScheduleData> stationSchedules = routeData.getStationSchedules().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> getSchedule(train, entry),
                       this::selectFirst , LinkedHashMap::new));
        return TrainData.builder()
                .id(train.getId())
                .number(train.getNumber())
                .places(train.getPlaces())
                .routeId(routeData.getId())
                .stationSchedules(stationSchedules)
                .status(train.getStatus().toString())
                .build();
    }

    private ScheduleData getSchedule(Train train, Map.Entry<String, ScheduleData> entry) {
        Optional<StationStatus> stationStatus = train.getStationStatuses().stream()
                .filter(status -> byStation(status, entry.getKey()))
                .findFirst();
        LocalDateTime departure = stationStatus
                .map(StationStatus::getDelay)
                .map(delay -> calculateResultTime(entry.getValue().getDeparture(), delay))
                .orElse(entry.getValue().getDeparture());
        LocalDateTime arrival = stationStatus
                .map(StationStatus::getDelay)
                .map(delay -> calculateResultTime(entry.getValue().getArrive(), delay))
                .orElse(entry.getValue().getArrive());
        String status = stationStatus
                .map(StationStatus::getStatus).map(Object::toString).orElse(StationStatus.Status.OK.toString());
        return entry.getValue().toBuilder()
                .arrive(arrival)
                .departure(departure)
                .status(status)
                .build();

    }

    private LocalDateTime calculateResultTime(LocalDateTime source, int delay) {
        return source.plusMinutes(delay);
    }

    private ScheduleData selectFirst(ScheduleData first, ScheduleData second) {
        return first;
    }

    private boolean byStation(StationStatus status, String station) {
        return Objects.equals(status.getStation().getName(), station);
    }
}
