package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.RouteEntry;
import com.tsystems.train.entity.Schedule;
import com.tsystems.train.facade.data.RouteData;
import com.tsystems.train.facade.data.ScheduleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RouteConverter implements DtoConverter<Route, RouteData> {

    @Autowired
    private DtoConverter<Schedule, ScheduleData> scheduleDataConverter;

    @Override
    public RouteData convert(Route source) {
        Map<String, ScheduleData> schedules = source.getRouteEntries().stream()
                .collect(Collectors.toMap(this::getStationName, this::getSchedule, this::getFirst, LinkedHashMap::new));
        return RouteData.builder()
                .id(source.getId())
                .stationSchedules(schedules)
                .status(source.getStatus().toString())
                .build();
    }

    private String getStationName(RouteEntry routeEntry) {
        return routeEntry.getStation().getName();
    }

    private ScheduleData getSchedule(RouteEntry routeEntry) {
        return scheduleDataConverter.convert(routeEntry.getSchedule());
    }

    private ScheduleData getFirst(ScheduleData first, ScheduleData second) {
        return first;
    }


}
