package com.tsystems.train.controllers;

import com.tsystems.train.facade.PassengerFacade;
import com.tsystems.train.facade.data.PassengerData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/passenger")
public class PassengerController extends BaseController{

    @Autowired
    private PassengerFacade passengerFacade;

    @GetMapping
    @Secured("ROLE_MANAGER")
    public List<PassengerData> getPassengers(@RequestParam String train) {
        log.debug("PassengerController: get passengers for train number {} request accepted", train);
        return passengerFacade.getPassengersForTrain(train);
    }
}
