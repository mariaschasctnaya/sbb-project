package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Station;
import com.tsystems.train.facade.data.StationData;
import org.springframework.stereotype.Component;

@Component
public class StationConverter implements DtoConverter<Station, StationData>{
    @Override
    public StationData convert(Station station) {
        return StationData.builder()
                .id(station.getId())
                .name(station.getName())
                .status(station.getStatus().toString())
                .build();
    }
}
