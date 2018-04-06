package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Station;
import com.tsystems.train.facade.data.StationData;
import org.springframework.stereotype.Component;

@Component
public class StationDataConverter implements DtoConverter<StationData, Station>{
    @Override
    public Station convert(StationData stationData) {
        return Station.builder()
                .name(stationData.getName())
                .build();
    }
}
