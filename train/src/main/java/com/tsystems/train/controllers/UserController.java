package com.tsystems.train.controllers;

import com.tsystems.train.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String createUser(@RequestParam("username") @NotNull String username,
                             @RequestParam("password") @NotNull String password) {
        userFacade.createUser(username, password);
        return "login";
    }
}
