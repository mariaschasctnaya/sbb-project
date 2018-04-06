package com.tsystems.train.exception;

import java.text.MessageFormat;

public class StationsScheduleHasWrongOrder extends RuntimeException {
    public StationsScheduleHasWrongOrder(String departureStation, String arriveStation) {
        super(MessageFormat.format("Route has wrong station order. Arrive time for station {0} is early than departure time for station {1}",
                arriveStation, departureStation));
    }

}
