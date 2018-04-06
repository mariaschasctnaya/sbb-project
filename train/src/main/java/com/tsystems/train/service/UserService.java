package com.tsystems.train.service;

import com.tsystems.train.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void create(User user);
    User getByName(String username);
}
