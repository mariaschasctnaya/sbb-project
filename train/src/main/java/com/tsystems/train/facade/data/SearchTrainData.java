package com.tsystems.train.facade.data;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchTrainData {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+")
    private String departure;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+")
    private String arrived;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
}
