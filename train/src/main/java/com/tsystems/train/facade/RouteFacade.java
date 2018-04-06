package com.tsystems.train.facade;


import com.tsystems.train.facade.data.RouteData;

import java.util.List;

public interface RouteFacade {

    /**
     * Finds routes by id
     *
     * @param id - id
     * @return routes
     */
    RouteData getRouteById(String id);

    /**
     * Gets routes by departure and arrive stations
     *
     * @param departureStation - departure station
     * @param arriveStation - arrive station
     * @return available routes between two given stations
     */
    List<RouteData> getRoutesByStations(String departureStation, String arriveStation);

    /**
     * Creates a new route
     *
     * @param routeData - route to create
     */
    void createRoute(RouteData routeData);

    /**
     * Gets routes by station
     *
     * @param station - station
     * @return all routes for the given station
     */
    List<RouteData> getRoutesByStation(String station);

    List<RouteData> getRoutes(boolean filter);

    void archiveRoute(String id);

    void archiveRouteByStation(String station);
}
