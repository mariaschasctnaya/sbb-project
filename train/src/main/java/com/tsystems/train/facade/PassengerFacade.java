package com.tsystems.train.facade;


import com.tsystems.train.facade.data.PassengerData;

import java.util.List;

public interface PassengerFacade {
    /**
     * Gets passengers for the given train
     *
     * @param trainNumber - train number
     * @return list of passenger's for the given train
     */
    List<PassengerData> getPassengersForTrain(String trainNumber);
}
