package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Schedule;
import com.tsystems.train.facade.data.ScheduleData;
import org.springframework.stereotype.Component;

@Component
public class ScheduleConverter implements DtoConverter<Schedule, ScheduleData> {
    @Override
    public ScheduleData convert(Schedule source) {
        return ScheduleData.builder()
                .arrive(source.getArrival())
                .departure(source.getDeparture())
                .build();
    }
}
