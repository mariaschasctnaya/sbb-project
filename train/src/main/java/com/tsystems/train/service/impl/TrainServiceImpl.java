package com.tsystems.train.service.impl;

import com.tsystems.train.entity.*;
import com.tsystems.train.exception.TrainIsOffException;
import com.tsystems.train.repository.TrainRepository;
import com.tsystems.train.service.RouteService;
import com.tsystems.train.service.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class TrainServiceImpl implements TrainService {

    private static final long minimumLeftMinutes = 10;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private RouteService routeService;

    @Override
    public Train createTrain(Train train) {
        log.debug("TrainService: check available ticket for train {}", train.getNumber());
        return trainRepository.save(train);
    }

    @Override
    public Train getTrainByNumber(String trainNumber) {
        return trainRepository.findByNumber(trainNumber);
    }

    @Override
    public void checkAvailableTrainByStation(Train train, String station) {
        log.debug("TrainService: check available train {} for station {}", train.getNumber(), station);
        Optional.ofNullable(train.getRoute())
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .map(Route::getRouteEntries).flatMap(Collection::stream)
                .filter(routeEntry -> filterRouteByStation(routeEntry, station))
                .map(RouteEntry::getSchedule)
                .findFirst()
                .ifPresent(this::checkSchedule);

    }

    private void checkSchedule(Schedule schedule) {
        long leftMinutes = LocalDateTime.now().until(schedule.getDeparture(), ChronoUnit.MINUTES);
        if (leftMinutes < minimumLeftMinutes) {
            throw new TrainIsOffException();
        }
    }

    private boolean filterRouteByStation(RouteEntry route, String station) {
        Optional<String> name = Optional.ofNullable(route.getStation()).map(Station::getName);
        return Optional.ofNullable(station).equals(name);
    }

    @Override
    public List<Train> searchTrain(String departureStation, String arrivedStation, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("TrainService: search train between {} and {} , and between {} and {}", departureStation, arrivedStation, startDate, endDate);
        List<Route> routes = routeService.getRoutesByStations(departureStation, arrivedStation).stream()
                .filter(route -> filterByDate(route, departureStation, arrivedStation, startDate, endDate))
                .collect(Collectors.toList());
        return trainRepository.findByRouteIn(routes);
    }

    private boolean filterByDate(Route route, String departureStation, String arrivedStation, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("TrainService: filter available trains for station {} by time between {} and {} ", departureStation, startDate, endDate);
        RouteEntry departureEntry = route.getRouteEntries().stream().filter(routeEntry -> routeEntry.getStation().getName().equals(departureStation)).findFirst().orElse(null);
        RouteEntry arrivalEntry = route.getRouteEntries().stream().filter(routeEntry -> routeEntry.getStation().getName().equals(arrivedStation)).findFirst().orElse(null);
        LocalDateTime departureTime = route.getRouteEntries().stream()
                .filter(routeEntry -> routeEntry.getStation().getName().equals(departureStation))
                .map(RouteEntry::getSchedule)
                .map(Schedule::getDeparture)
                .findFirst()
                .orElse(LocalDateTime.now());
        return route.getRouteEntries().indexOf(departureEntry) < route.getRouteEntries().indexOf(arrivalEntry)
                && departureTime.isAfter(LocalDateTime.now())
                && (departureTime.isAfter(startDate) || departureTime.isEqual(startDate))
                && (departureTime.isBefore(endDate) || departureTime.isEqual(endDate));
    }

    @Override
    public List<Train> getTrains(String station) {
        log.debug("TrainService: get trains for station {}", station);
        if(station == null) {
            return trainRepository.findAll();
        }
        else {
            List<Route> routes = routeService.getRoutesByStation(station);
            return trainRepository.findByRouteIn(routes);
        }
    }

    @Override
    public Train getTrainById(String id) {
        return trainRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void archiveTrainByRoute(Route route) {
        trainRepository.updateStatusByRoute(route, EntityStatus.ARCHIVED);
    }

    @Override
    @Transactional
    public void archive(Train train) {
        trainRepository.updateStatus(train.getId(), EntityStatus.ARCHIVED);
    }

    @Override
    public List<Train> getTrainsByRoute(Route route) {
        return trainRepository.findByRouteIn(Collections.singleton(route));
    }
}
