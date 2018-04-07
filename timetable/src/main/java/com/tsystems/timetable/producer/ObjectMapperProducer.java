package com.tsystems.timetable.producer;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.inject.Produces;


public class ObjectMapperProducer {
// Deserialize in Java objects
    @Produces
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
