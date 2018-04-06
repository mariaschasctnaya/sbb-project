package com.tsystems.train.exception;

public class PassengerExistsException extends RuntimeException {
    public PassengerExistsException() {
        super("Passenger was already registered");
    }
}
