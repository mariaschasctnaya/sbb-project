package com.tsystems.train.facade.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketData {
    @NotNull
    @Pattern(regexp = ".*[\\S]+.*")
    private String trainNumber;
    @NotNull @Valid
    private PassengerData passenger;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+")
    private String station;
}

