package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.*;
import com.tsystems.train.facade.data.PassengerData;
import com.tsystems.train.facade.data.TicketData;
import com.tsystems.train.service.StationService;
import com.tsystems.train.service.TrainService;
import com.tsystems.train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TicketDataConverter implements DtoConverter<TicketData, Ticket> {

    @Autowired
    private DtoConverter<PassengerData, Passenger> passengerConverter;
    @Autowired
    private TrainService trainService;
    @Autowired
    private StationService stationService;
    @Autowired
    private UserService userService;

    @Override
    public Ticket convert(TicketData ticketData) {
        Train train = trainService.getTrainByNumber(ticketData.getTrainNumber());
        Passenger passenger = passengerConverter.convert(ticketData.getPassenger());
        Station station = stationService.getStationByName(ticketData.getStation());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByName(authentication.getName());
        return Ticket.builder()
                .train(train)
                .startStation(station)
                .user(user)
                .passenger(passenger)
                .build();
    }
}
