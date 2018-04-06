package com.tsystems.train.facade.impl;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.entity.Ticket;
import com.tsystems.train.entity.Train;
import com.tsystems.train.facade.PassengerFacade;
import com.tsystems.train.facade.converter.DtoConverter;
import com.tsystems.train.facade.data.PassengerData;
import com.tsystems.train.service.TicketService;
import com.tsystems.train.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PassengerFacadeImpl implements PassengerFacade {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private DtoConverter<Passenger, PassengerData> passengerConverter;

    @Override
    public List<PassengerData> getPassengersForTrain(String trainNumber) {
        Train train = trainService.getTrainByNumber(trainNumber);
        return ticketService.getTicketForTrain(train).stream()
                .map(Ticket::getPassenger)
                .map(passengerConverter::convert)
                .collect(Collectors.toList());
    }
}
