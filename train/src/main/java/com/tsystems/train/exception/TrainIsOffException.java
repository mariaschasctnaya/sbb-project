package com.tsystems.train.exception;


public class TrainIsOffException extends RuntimeException {
    public TrainIsOffException() {
        super("Train is off");
    }
}
