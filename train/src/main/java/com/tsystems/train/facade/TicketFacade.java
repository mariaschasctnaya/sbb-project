package com.tsystems.train.facade;


import com.tsystems.train.exception.TicketSoldOutException;
import com.tsystems.train.exception.TrainIsOffException;
import com.tsystems.train.facade.data.TicketData;

import java.util.List;

public interface TicketFacade {
    /**
     * Creates a new ticket and send request for train-service for registration
     * @param ticket - ticket to check and save
     * @return ticket number
     *
     * @throws TicketSoldOutException if every ticket had been sold
     * @throws TrainIsOffException if train had been off
     */
    String createTicket(TicketData ticket);

    /**
     * Return number of tickets for train with number trainNumber
     * @param trainNumber
     * @return count of tickets
     */
    int findCountTicketByTrain(String trainNumber);

    /**
     * Return ticket which belongs to user
     * @param username - name of user
     * @return tickets for user
     */
    List<TicketData> getTicketsForUser(String username);
}
