package com.tsystems.timetable.jsfController;

import com.tsystems.timetable.KafkaInitializer;
import com.tsystems.timetable.TimetableInfoLoader;
import com.tsystems.timetable.dto.TrainTimetable;
import lombok.extern.log4j.Log4j;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@RequestScoped
@Log4j
public class TimetableController {

    @Inject
    private TimetableInfoLoader timetableInfoLoader;

    @Inject
    private KafkaInitializer kafkaInitializer;

    private final String station;

    public TimetableController() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        station = (String) req.getAttribute("station");
    }

    @PostConstruct
    public void init() {
        kafkaInitializer.subscribe(station);
    }

    public String getStation(){
        return station;
    }

    public Collection<TrainTimetable> getTrainTimetableList(){
        Collection<TrainTimetable> trainTables = timetableInfoLoader.getStationsTimetableStorage().get(station);
        if(trainTables == null || trainTables.isEmpty()) {
            timetableInfoLoader.init();
            trainTables = timetableInfoLoader.getStationsTimetableStorage().get(station);
            if(trainTables == null)
                trainTables = new ArrayList<>();
        }
        return trainTables.stream().filter(this::byToday).collect(Collectors.toList());
    }

    private boolean byToday(TrainTimetable trainTimetable) {
        LocalDate departure = LocalDateTime.parse(trainTimetable.getDepartureTime()).toLocalDate();
        LocalDate arrive = LocalDateTime.parse(trainTimetable.getArrivedTime()).toLocalDate();
        LocalDate now = LocalDate.now();
        return departure.isEqual(now) || arrive.isEqual(now);
    }

}
