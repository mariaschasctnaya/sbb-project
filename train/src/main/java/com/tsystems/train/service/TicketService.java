package com.tsystems.train.service;

import com.tsystems.train.entity.Ticket;
import com.tsystems.train.entity.Train;
import com.tsystems.train.entity.User;
import com.tsystems.train.exception.TicketSoldOutException;

import java.util.List;

public interface TicketService {

    /**
     * Checks for given ticket if they not existing then saves it
     * @param ticket - ticket to check and save
     * @return ticket
     */
    Ticket createTicket(Ticket ticket);

    /**
     *  Gets all sold tickets for the given train
     * @param train - train to search tickets for this trains
     * @return tickets list for the given trains
     */
    List<Ticket> getTicketForTrain(Train train);

    /**
     * Check available tickets
     * @throws TicketSoldOutException if every ticket had been sold
     * @param train - train to search sold tickets for this train
     */
    void checkAvailableTickets(Train train);

    /**
     * Return tickets belong user
     * @param user
     * @return tickets
     */
    List<Ticket> getTicketsForUser(User user);
}
