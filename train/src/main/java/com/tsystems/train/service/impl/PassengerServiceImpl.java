package com.tsystems.train.service.impl;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.repository.PassengerRepository;
import com.tsystems.train.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public Passenger refresh(Passenger passenger) {
        log.debug("PassengerService: searching {} in repository", passenger);
        Passenger refreshed = passengerRepository.findByNameAndSurnameAndBirthday(passenger.getName(),
                passenger.getSurname(), passenger.getBirthday());
        return refreshed == null ? passenger : refreshed;
    }
}

