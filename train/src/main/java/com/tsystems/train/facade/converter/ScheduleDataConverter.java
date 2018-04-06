package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Schedule;
import com.tsystems.train.facade.data.ScheduleData;
import org.springframework.stereotype.Component;

@Component
public class ScheduleDataConverter implements DtoConverter<ScheduleData, Schedule> {
    @Override
    public Schedule convert(ScheduleData source) {
        return Schedule.builder()
                .departure(source.getDeparture())
                .arrival(source.getArrive())
                .build();
    }
}
