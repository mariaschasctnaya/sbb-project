package com.tsystems.train.facade.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Station;
import com.tsystems.train.facade.RouteFacade;
import com.tsystems.train.facade.StationFacade;
import com.tsystems.train.facade.converter.DtoConverter;
import com.tsystems.train.facade.data.StationData;
import com.tsystems.train.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class StationFacadeImpl implements StationFacade {

    @Autowired
    private StationService stationService;
    @Autowired
    private DtoConverter<StationData, Station> stationDataConverter;
    @Autowired
    private DtoConverter<Station, StationData> stationConverter;
    @Autowired
    private RouteFacade routeFacade;

    @Override
    public void createStation(StationData stationData) {
        Station station = stationDataConverter.convert(stationData);
        stationService.createStation(station);
    }

    @Override
    public List<StationData> getStations(String station, boolean filter) {
        Stream<Station> stations = getStations(station);
        if(filter) {
            stations = stations.filter(this::filterByStatus);
        }
        return stations.map(stationConverter::convert).collect(Collectors.toList());
    }

    @Override
    public void archiveStation(String id) {
        Station station = stationService.getStationById(id);
        stationService.archive(station);
        routeFacade.archiveRouteByStation(station.getName());
    }

    private Stream<Station> getStations(String station) {
        return stationService.getStations(station).stream();
    }

    private boolean filterByStatus(Station station) {
        return station.getStatus() == EntityStatus.ACTIVE;
    }
}
