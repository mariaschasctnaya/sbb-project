package com.tsystems.train.facade.data;


import com.fasterxml.jackson.annotation.JsonView;
import com.tsystems.train.facade.json.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationData {

    @JsonView(View.Server.class)
    private String id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+")
    @JsonView(View.UI.class)
    private String name;

    @JsonView(View.Server.class)
    private String status;

}
