package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.entity.Ticket;
import com.tsystems.train.facade.data.PassengerData;
import com.tsystems.train.facade.data.TicketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverter implements DtoConverter<Ticket, TicketData> {

    @Autowired
    private DtoConverter<Passenger, PassengerData> passengerConverter;

    @Override
    public TicketData convert(Ticket source) {
        return TicketData.builder()
                .passenger(passengerConverter.convert(source.getPassenger()))
                .station(source.getStartStation().getName())
                .trainNumber(source.getTrain().getNumber())
                .build();
    }
}
