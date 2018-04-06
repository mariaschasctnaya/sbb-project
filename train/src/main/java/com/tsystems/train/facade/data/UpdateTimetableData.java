package com.tsystems.train.facade.data;

import lombok.Data;

@Data
public class UpdateTimetableData {
    private String trainNumber;
    private String status;
    private String arrive;
    private String departure;
    private Integer delayTime;
    private boolean refresh;
}
