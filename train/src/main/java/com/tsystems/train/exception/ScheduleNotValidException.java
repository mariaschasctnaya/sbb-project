package com.tsystems.train.exception;


import java.text.MessageFormat;

public class ScheduleNotValidException extends RuntimeException {
    public ScheduleNotValidException(String station) {
        super(MessageFormat.format("Schedule for station {0} is not valid. Departure time is early then arrive time",
                station));
    }

}
