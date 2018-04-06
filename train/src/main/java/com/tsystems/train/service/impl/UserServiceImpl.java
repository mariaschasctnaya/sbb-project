package com.tsystems.train.service.impl;

import com.tsystems.train.entity.Role;
import com.tsystems.train.entity.User;
import com.tsystems.train.repository.UserRepository;
import com.tsystems.train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void create(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByName(String username) {
        return userRepository.findByName(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findByName(name)
                .map(this::toUserDetails)
                .orElse(anonymousUser(name));
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .authorities("ROLE_" + user.getRole().toString())
                .username(user.getName())
                .password(user.getPassword())
                .build();
    }

    private UserDetails anonymousUser(String name) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .authorities("ROLE_" + Role.ANONYMOUS)
                .username(name)
                .password("")
                .build();
    }
}
