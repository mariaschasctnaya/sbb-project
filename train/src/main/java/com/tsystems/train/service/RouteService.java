package com.tsystems.train.service;


import com.tsystems.train.entity.Route;

import java.util.List;

public interface RouteService {


    /**
     * Gets routes by departure and arrive stations
     *
     * @param departureStation - departure station
     * @param arriveStation - arrive station
     * @return available routes between two given stations
     */
    List<Route> getRoutesByStations(String departureStation, String arriveStation);

    /**
     * Creates a new route
     *
     * @param route - route to create
     */
    void createRoute(Route route);

    /**
     * Gets routes by station
     *
     * @param station - station
     * @return all routes for the given station
     */
    List<Route> getRoutesByStation(String station);

    /**
     * Gets all routes
     * @return routes
     */
    List<Route> getRoutes();

    /**
     * Get route by id
     * @param id -route id
     * @return route
     */
    Route getRouteById(String id);

    void archive(Route route);
}
