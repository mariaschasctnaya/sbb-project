package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.RouteEntry;
import com.tsystems.train.entity.StationStatus;
import com.tsystems.train.entity.Train;
import com.tsystems.train.facade.data.TrainData;
import com.tsystems.train.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateTrainDataConverter implements DtoConverter<TrainData, Train> {

    @Autowired
    private RouteService routeService;

    @Override
    public Train convert(TrainData source) {
        Route route = routeService.getRouteById(source.getRouteId());
        List<StationStatus> statuses = route.getRouteEntries().stream()
                .map(this::toStationStatus)
                .collect(Collectors.toList());
        Train.TrainBuilder builder = Train.builder();

        Train train = builder
                .number(source.getNumber())
                .places(source.getPlaces())
                .route(route)
                .stationStatuses(statuses)
                .build();
        train.getStationStatuses().forEach(status -> status.setTrain(train));
        return train;
    }

    private StationStatus toStationStatus(RouteEntry routeEntry) {
        return StationStatus.builder()
                .delay(0)
                .station(routeEntry.getStation())
                .build();
    }
}
