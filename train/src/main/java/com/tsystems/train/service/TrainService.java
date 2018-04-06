package com.tsystems.train.service;


import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.Train;
import com.tsystems.train.exception.TrainIsOffException;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainService {

    /**
     * Creates a new train and saves it
     *
     * @param train - trains that need to be saved
     * @return trains
     */
    Train createTrain(Train train);

    /**Gets trains by number
     *
     * @param trainNumber - train number
     * @return train
     */
    Train getTrainByNumber(String trainNumber);

    /**
     * Checks available trains for given station, that have  at least 10 minutes before the departure
     * @param train - train
     * @param station - station
     *
     * @throws TrainIsOffException if there are less than 10 minutes before the departure
     */
    void checkAvailableTrainByStation(Train train, String station);

    /**
     * Search train for given departure and arrive stations for the given date
     *
     * @param departureStation - departure station
     * @param arrivedStation - arrived station
     * @param startDate - departure date
     * @return list of available trains
     */
    List<Train> searchTrain(String departureStation, String arrivedStation, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Gets all trains for the given station
     * @param station - station
     * @return list of available trains
     */
    List<Train> getTrains(String station);

    /**
     * Gets train by id
     * @param id - train id
     * @return train
     */
    Train getTrainById(String id);

    /**
     * Set status for train to archive
     * @param route - route
     */
    void archiveTrainByRoute(Route route);


    /**
     * с
     * @param train - train
     */
    void archive(Train train);

    /**
     * Gets all trains by route
     * @param route - route id
     * @return - trains
     */
    List<Train> getTrainsByRoute(Route route);
}
