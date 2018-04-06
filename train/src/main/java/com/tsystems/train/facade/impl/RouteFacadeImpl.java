package com.tsystems.train.facade.impl;

import com.tsystems.train.entity.EntityStatus;
import com.tsystems.train.entity.Route;
import com.tsystems.train.facade.RouteFacade;
import com.tsystems.train.facade.converter.DtoConverter;
import com.tsystems.train.facade.data.RouteData;
import com.tsystems.train.service.RouteService;
import com.tsystems.train.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RouteFacadeImpl implements RouteFacade {

    @Autowired
    private TrainService trainService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private DtoConverter<Route, RouteData> routeConverter;
    @Autowired
    private DtoConverter<RouteData, Route> routeDataConverter;

    @Override
    public RouteData getRouteById(String id) {
        return routeConverter.convert(routeService.getRouteById(id));
    }

    @Override
    public List<RouteData> getRoutesByStations(String departureStation, String arriveStation) {
        return routeService.getRoutesByStations(departureStation,arriveStation).stream()
                .filter(this::filterByStatus)
                .map(routeConverter::convert).collect(Collectors.toList());
    }

    @Override
    public void createRoute(RouteData routeData) {
        routeService.createRoute(routeDataConverter.convert(routeData));
    }

    @Override
    public List<RouteData> getRoutesByStation(String station) {
        return routeService.getRoutesByStation(station).stream()
                .filter(this::filterByStatus)
                .map(routeConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<RouteData> getRoutes(boolean filter) {
        Stream<Route> routes = getRoutes();
        if(filter)
            routes = routes.filter(this::filterByStatus);
        return routes.map(routeConverter::convert).collect(Collectors.toList());
    }

    @Override
    public void archiveRoute(String id) {
        Route route = routeService.getRouteById(id);
        routeService.archive(route);
        trainService.archiveTrainByRoute(route);
    }

    @Override
    public void archiveRouteByStation(String station) {
        List<Route> routesByStation = routeService.getRoutesByStation(station);
        routesByStation.stream().map(Route::getId).forEach(this::archiveRoute);
    }

    private Stream<Route> getRoutes() {
        return routeService.getRoutes().stream();
    }

    private boolean filterByStatus(Route route) {
        return route.getStatus() == EntityStatus.ACTIVE;
    }
}
