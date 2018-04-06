package com.tsystems.train.service.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.RouteEntry;
import com.tsystems.train.entity.Station;
import com.tsystems.train.repository.RouteRepository;
import com.tsystems.train.repository.StationRepository;
import com.tsystems.train.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RouteServiceImpl implements RouteService {


    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;

    @Override
    public List<Route> getRoutesByStations(String departureStation, String arriveStation) {
        log.debug("RouteService: find {} station in repository", departureStation);
        Station departureStationEntity = stationRepository.findByName(departureStation);
        log.debug("RouteService: find {} station in repository", arriveStation);
        Station arriveStationEntity = stationRepository.findByName(arriveStation);
        if(departureStationEntity == null || arriveStationEntity == null)
            return Collections.emptyList();
        log.debug("RouteService: find routes between {} and {} stations in repository", departureStationEntity.getName(), arriveStationEntity.getName());
        List<Route> foundedRoutes = routeRepository.findByStations(departureStationEntity, arriveStationEntity);
        log.debug("RouteService: found {} routes", foundedRoutes.size());
        return foundedRoutes.stream().filter(route -> filterRoute(route, departureStation, arriveStation)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createRoute(Route route) {
        routeRepository.save(route);
    }

    @Override
    public List<Route> getRoutesByStation(String station) {
        log.debug("RouteService: find {} station in repository", station);
        Station stationEntity = stationRepository.findByName(station);
        log.debug("RouteService: foud {} station in repository", station);
        if(stationEntity == null)
            throw new RuntimeException();
        return routeRepository.findByStation(stationEntity);
    }

    @Override
    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Route getRouteById(String id) {
        return routeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void archive(Route route) {
        routeRepository.updateStatus(route.getId(), EntityStatus.ARCHIVED);
    }

    private boolean filterRoute(Route route, String departureStation, String arriveStation) {
        for(RouteEntry routeEntry : route.getRouteEntries()) {
            if(Objects.equals(routeEntry.getStation().getName(), departureStation) || Objects.equals(routeEntry.getStation().getName(), arriveStation)) {
                return Objects.equals(routeEntry.getStation().getName(), departureStation);
            }
        }
        return false;
    }
}
