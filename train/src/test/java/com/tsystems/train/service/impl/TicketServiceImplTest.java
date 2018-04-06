package com.tsystems.train.service.impl;

import com.tsystems.train.entity.Passenger;
import com.tsystems.train.entity.Ticket;
import com.tsystems.train.entity.Train;
import com.tsystems.train.exception.TicketSoldOutException;
import com.tsystems.train.repository.TicketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {
    @Mock
    private TicketRepository ticketRepositoryMock;

    @InjectMocks
    private TicketServiceImpl ticketServiceImpl;


    private Ticket ticket;
    private Train train;

    @Before
    public void setUp(){
        ticket= new Ticket();
        ticket.setNumber("10");
        ticket.setTrain(train);
    }

    @Test
    public void createTicketTest(){
        Passenger passenger = new Passenger();
        passenger.setId("10");
        ticket.setPassenger(passenger);
        when(ticketRepositoryMock.existsByTrainAndPassenger(any(Train.class),any(Passenger.class))).thenReturn(false);
        ticketServiceImpl.createTicket(ticket);
        verify(ticketRepositoryMock,times(1)).save(ticket);
    }

    @Test
    public void  getTicketForTrainTest(){
        ticketServiceImpl.getTicketForTrain(train);
        verify(ticketRepositoryMock,times(1)).findByTrain(train);
    }

    @Test(expected = TicketSoldOutException.class)
    public void checkAvailableTicketsTest(){
        train = new Train();
        when(ticketRepositoryMock.countAllByTrain(any(Train.class))).thenReturn(10);
        train.setPlaces(10);
        ticketServiceImpl.checkAvailableTickets(train);
    }
}
