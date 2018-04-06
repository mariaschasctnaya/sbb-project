package com.tsystems.train.facade.data;


import com.fasterxml.jackson.annotation.JsonView;
import com.tsystems.train.facade.json.View;
import com.tsystems.train.validation.constraint.ScheduleConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteData {
    @JsonView(View.Server.class)
    private String id;
    @JsonView(View.Server.class)
    private String status;

    @NotNull
    @Size(min = 2)
    @ScheduleConstraint
    @JsonView(View.UI.class)
    @Valid
    private Map<String, ScheduleData> stationSchedules;
}
