package com.tsystems.train.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tsystems.train.facade.TrainFacade;
import com.tsystems.train.facade.data.ResponseData;
import com.tsystems.train.facade.data.SearchTrainData;
import com.tsystems.train.facade.data.TrainData;
import com.tsystems.train.facade.data.UpdateTrainData;
import com.tsystems.train.facade.json.View;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/train")
@Slf4j
public class TrainController {

    @Autowired
    private TrainFacade trainFacade;

    @PostMapping
    @Secured("ROLE_MANAGER")
    public ResponseData createTrain(@RequestBody @Valid @JsonView(View.UI.class) TrainData trainData) {
        log.debug("TrainController: train {} for {} places creation request accepted", trainData.getNumber(), trainData.getPlaces());
        trainFacade.createTrain(trainData);
        return new ResponseData();
    }
    @GetMapping
    public List<TrainData> getTrains(@RequestParam(value = "q", required = false) String station) {
        log.debug("TrainController: get trains request accepted {}", station);
        return trainFacade.getTrains(station, true);
    }

    @GetMapping("/search")
    public List<TrainData> searchTrains(@RequestBody @RequestParam("departure") String departure, @RequestParam("arrived") String arrived,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("startDate") LocalDateTime startDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("endDate") LocalDateTime endDate) {
        SearchTrainData searchTrainData = new SearchTrainData();
        searchTrainData.setArrived(arrived);
        searchTrainData.setDeparture(departure);
        searchTrainData.setEndDate(endDate);
        searchTrainData.setStartDate(startDate);
        log.debug("TrainController: search available trains for {} between {} and {}", searchTrainData.getStartDate(), searchTrainData.getDeparture(), searchTrainData.getArrived());
        return trainFacade.searchTrains(searchTrainData);
    }

    @PutMapping("/status")
    @Secured("ROLE_MANAGER")
    public ResponseData updateStatus(@RequestBody UpdateTrainData data) {
        trainFacade.updateStatus(data.getTrainNumber(), data.getStation(), data.getStatus(), data.getDelayTime());
        return new ResponseData();
    }



    @DeleteMapping("/{id}")
    @Secured("ROLE_MANAGER")
    public ResponseData archiveTrain(@PathVariable String id) {
        trainFacade.archiveTrain(id);
        return new ResponseData();
    }

    @GetMapping("/all")
    @Secured("ROLE_MANAGER")
    public List<TrainData> getAllTrains() {
        return trainFacade.getTrains(null, false);
    }

    @DeleteMapping
    public void archiveByRoutes(@RequestBody String routeId) {
        trainFacade.archiveTrainByRoute(routeId);
    }


}
