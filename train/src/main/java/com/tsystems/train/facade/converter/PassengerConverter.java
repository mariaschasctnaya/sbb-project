package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.facade.data.PassengerData;
import org.springframework.stereotype.Component;

@Component
public class PassengerConverter implements DtoConverter<Passenger, PassengerData>{
    @Override
    public PassengerData convert(Passenger passenger) {
        return PassengerData.builder()
                .name(passenger.getName())
                .surname(passenger.getSurname())
                .birthday(passenger.getBirthday())
                .build();
    }
}
