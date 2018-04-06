package com.tsystems.train.facade.impl;

import com.tsystems.train.entity.User;
import com.tsystems.train.facade.UserFacade;
import com.tsystems.train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(String username,String password) {
        userService.create(User.builder()
                .name(username)
                .password(passwordEncoder.encode(password))
                .build());
    }
}
