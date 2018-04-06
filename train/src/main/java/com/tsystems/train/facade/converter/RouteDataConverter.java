package com.tsystems.train.facade.converter;

import com.tsystems.train.entity.Route;
import com.tsystems.train.entity.RouteEntry;
import com.tsystems.train.entity.Schedule;
import com.tsystems.train.entity.Station;
import com.tsystems.train.facade.data.RouteData;
import com.tsystems.train.facade.data.ScheduleData;
import com.tsystems.train.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RouteDataConverter implements DtoConverter<RouteData, Route> {

    @Autowired
    private StationService stationService;
    @Autowired
    private DtoConverter<ScheduleData, Schedule> scheduleDataConverter;

    @Override
    public Route convert(RouteData source) {
        List<RouteEntry> routeEntries = source.getStationSchedules().entrySet().stream()
                .map(this::toRouteEntry)
                .collect(Collectors.toCollection(LinkedList::new));
        Route route = Route.builder().routeEntries(routeEntries).build();
        route.getRouteEntries().forEach(routeEntry -> routeEntry.setRoute(route));
        return route;
    }

    private RouteEntry toRouteEntry(Map.Entry<String, ScheduleData> entryStationSchedule) {
        Station station = stationService.getStationByName(entryStationSchedule.getKey());
        Schedule schedule = scheduleDataConverter.convert(entryStationSchedule.getValue());
        return RouteEntry.builder()
                .station(station)
                .schedule(schedule)
                .build();
    }
}
