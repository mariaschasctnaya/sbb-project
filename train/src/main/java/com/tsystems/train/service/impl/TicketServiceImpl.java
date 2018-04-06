package com.tsystems.train.service.impl;

import com.tsystems.train.entity.Ticket;
import com.tsystems.train.entity.Train;
import com.tsystems.train.entity.User;
import com.tsystems.train.exception.PassengerExistsException;
import com.tsystems.train.exception.TicketSoldOutException;
import com.tsystems.train.repository.TicketRepository;
import com.tsystems.train.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {


    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    @Override
    public Ticket createTicket(Ticket ticket) {
        checkExistingPassengers(ticket);
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketForTrain(Train train) {
        log.debug("TicketService: get tickets for train {}", train);
        return ticketRepository.findByTrain(train);
    }

    @Override
    public void checkAvailableTickets(Train train) {
        log.debug("TicketService: get count of sold tickets fo {}", train);
        int countSoldTicket = ticketRepository.countAllByTrain(train);
        if(train.getPlaces() <= countSoldTicket)
        {
            throw new TicketSoldOutException();
        }
    }

    @Override
    public List<Ticket> getTicketsForUser(User user) {
        return ticketRepository.findByUser(user);
    }

    private void checkExistingPassengers(Ticket ticket) {
        log.debug("TicketService: check passenger {}", ticket.getPassenger());
        if (ticket.getPassenger().getId() != null) {
            if (ticketRepository.existsByTrainAndPassenger(ticket.getTrain(), ticket.getPassenger())) {
                throw new PassengerExistsException();
            }
        }
    }
}