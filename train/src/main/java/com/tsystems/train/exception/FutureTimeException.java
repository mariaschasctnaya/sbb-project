package com.tsystems.train.exception;

public class FutureTimeException extends RuntimeException {
    public FutureTimeException() {
        super("Date can't be in future");
    }
}
