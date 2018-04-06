package com.tsystems.train.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import com.tsystems.train.facade.RouteFacade;
import com.tsystems.train.facade.data.ResponseData;
import com.tsystems.train.facade.data.RouteData;
import com.tsystems.train.facade.json.View;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/route")
public class RoutesController extends BaseController{

    @Autowired
    private RouteFacade routeFacade;

    @PostMapping
    @Secured("ROLE_MANAGER")
    public ResponseData createRoute(@RequestBody @Valid @JsonView(View.UI.class) RouteData routeData) {
        log.debug("RoutesController: {} create request accepted", routeData.getId());
        routeFacade.createRoute(routeData);
        return new ResponseData();
    }



    @GetMapping("/{id}")
    @JsonView(View.Server.class)
    public RouteData getRoute(@PathVariable("id") String id) {
        log.debug("RoutesController: find route by id {} request accepted", id);
        return routeFacade.getRouteById(id);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_MANAGER")
    public ResponseData archiveRoute(@PathVariable String id) {
        routeFacade.archiveRoute(id);
        return new ResponseData();
    }

    @GetMapping
    public List<RouteData> searchByStations(@RequestParam(required = false) String departureStation,
                                            @RequestParam(required = false) String arriveStation,
                                            @RequestParam(required = false) String station) {
        log.debug("RoutesController: search request accepted");
        if(station != null)  {
            log.debug("RoutesController: search routes by station {}", station);
            return routeFacade.getRoutesByStation(station);
        }
        else {
            log.debug("RoutesController: search routes from {} to {}", departureStation, arriveStation);
            return (departureStation == null && arriveStation == null) ? routeFacade.getRoutes(true) :
                    ((departureStation == null || arriveStation == null) ? Collections.emptyList() :
                            routeFacade.getRoutesByStations(departureStation, arriveStation));
        }
    }

    @GetMapping("/all")
    @Secured("ROLE_MANAGER")
    @JsonView(View.Server.class)
    public List<RouteData> getAllRoutes() {
        return routeFacade.getRoutes(false);
    }

}
