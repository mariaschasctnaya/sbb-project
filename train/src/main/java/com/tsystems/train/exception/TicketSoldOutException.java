package com.tsystems.train.exception;

public class TicketSoldOutException extends RuntimeException {
    public TicketSoldOutException() {
        super("All tickets are sold");
    }
}
