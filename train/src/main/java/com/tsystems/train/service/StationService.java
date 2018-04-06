package com.tsystems.train.service;



import com.tsystems.train.entity.Station;

import java.util.List;

public interface StationService {

    /**
     * Creates station
     *
     * @param station - station
     */
    void createStation(Station station);

    /**
     * Gets station like a station
     *
     * @param station - station name
     * @return stations
     */
    List<Station> getStations(String station);

    /**
     * Gets station by name
     *
     * @param name - station name
     * @return station
     */
    Station getStationByName(String name);

    /**
     * Gets station by id
     * @param id - id
     * @return station
     */
    Station getStationById(String id);

    /**
     * Archive station
     * @param station - station
     */
    void archive(Station station);
}
