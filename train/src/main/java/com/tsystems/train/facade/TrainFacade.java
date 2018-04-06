package com.tsystems.train.facade;


import com.tsystems.train.facade.data.SearchTrainData;
import com.tsystems.train.facade.data.TrainData;

import java.util.List;

public interface TrainFacade {
    void createTrain(TrainData trainData);

    /**
     * Search train for given departure and arrive stations for the given date
     * @param searchTrainData - search train data
     * @return list of trains
     */
    List<TrainData> searchTrains(SearchTrainData searchTrainData);

    /**
     * Checks available trains for given station
     *
     * @param station - station
     * @return - list of trains
     */
    List<TrainData> getTrains(String station, boolean filter);

    /**
     * Updates status for given station and train number
     * @param trainNumber - train number
     * @param station - station name
     * @param status - new status
     * @param delayTime
     */
    void updateStatus(String trainNumber, String station, String status, Integer delayTime);

    /**
     * Archive given train
     * @param id - train id
     */
    void archiveTrain(String id);

    /**
     * Archive all trains for given route
     * @param routeId - route id
     */
    void archiveTrainByRoute(String routeId);
}
