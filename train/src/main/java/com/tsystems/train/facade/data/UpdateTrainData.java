package com.tsystems.train.facade.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTrainData {
    private String trainNumber;
    private String station;
    private Integer delayTime;
    private String status;
}
