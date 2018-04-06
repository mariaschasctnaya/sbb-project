package com.tsystems.train.service;


import com.tsystems.train.entity.Passenger;

public interface PassengerService {

    /**
     * Refreshes given passenger from repository
     *
     * @param passenger - passenger
     */
    Passenger refresh(Passenger passenger);
}
