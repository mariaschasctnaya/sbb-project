package com.tsystems.timetable.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "shift")
public class TrainTimetable {
    private String id;
    private String arrivedTime;
    private String departureTime;
    private String status;
    private Integer shift;
}
