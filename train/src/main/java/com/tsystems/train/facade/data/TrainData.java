package com.tsystems.train.facade.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.tsystems.train.facade.json.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainData {

    @JsonView(View.Server.class)
    private String id;

    @JsonView(View.Server.class)
    private String status;

    @JsonView(View.Server.class)
    private Map<String, ScheduleData> stationSchedules;

    @NotNull
    @Pattern(regexp = ".*[\\S]+.*")
    @JsonView(View.UI.class)
    private String number;

    @NotNull
    @JsonView(View.UI.class)
    private String routeId;

    @Min(1)
    @JsonView(View.UI.class)
    private int places;
}
