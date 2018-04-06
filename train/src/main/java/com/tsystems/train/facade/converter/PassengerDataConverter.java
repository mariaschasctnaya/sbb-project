package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.facade.data.PassengerData;
import com.tsystems.train.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassengerDataConverter implements DtoConverter<PassengerData, Passenger> {

    @Autowired
    private PassengerService passengerService;

    @Override
    public Passenger convert(PassengerData passengerData) {
        Passenger passenger = Passenger.builder()
                .name(passengerData.getName())
                .birthday(passengerData.getBirthday())
                .surname(passengerData.getSurname())
                .build();
        passenger = passengerService.refresh(passenger);
        return passenger;
    }
}
