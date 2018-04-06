package com.tsystems.timetable.producer;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.inject.Produces;


public class ObjectMapperProducer {

    @Produces
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
