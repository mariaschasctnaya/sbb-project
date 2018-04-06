package com.tsystems.train.facade;


import com.tsystems.train.facade.data.StationData;

import java.util.List;


public interface StationFacade {

    /**
     * Creates station
     *
     * @param stationData - station
     */
    void createStation(StationData stationData);

    /**
     * Gets station like a station
     *
     * @param station - station name
     * @return stationData
     */
    List<StationData> getStations(String station, boolean filter);

    void archiveStation(String id);
}
