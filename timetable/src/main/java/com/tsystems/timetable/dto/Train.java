package com.tsystems.timetable.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Train {
    private int places;
    private String number;
    private String routeId;
    private String id;
    private String status;
    private Map<String, Timetable> stationSchedules;
}
