package com.tsystems.train.service;

public interface MessageService {
    void send(String topic, Object message);
}
