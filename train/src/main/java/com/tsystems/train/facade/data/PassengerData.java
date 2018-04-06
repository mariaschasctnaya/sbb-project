package com.tsystems.train.facade.data;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.tsystems.train.validation.constraint.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PassengerData {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+")
    private String name;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+")
    private String surname;
    @NotNull
    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthday;
}

