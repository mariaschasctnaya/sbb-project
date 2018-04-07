package com.tsystems.train.controllers;


import com.tsystems.train.exception.PassengerExistsException;
import com.tsystems.train.exception.ScheduleNotValidException;
import com.tsystems.train.exception.StationsScheduleHasWrongOrder;
import com.tsystems.train.facade.data.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@Slf4j
public class BaseController {

    @ExceptionHandler(ScheduleNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseData handleScheduleValidationException(ScheduleNotValidException ex) {
        return buildErrorResponse(ex);
    }


    @ExceptionHandler(StationsScheduleHasWrongOrder.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseData handleScheduleOrderValidationException(StationsScheduleHasWrongOrder ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseData handleCommonException(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(PassengerExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseData handlePassengerExistsException(PassengerExistsException ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseData handleValidationException(MethodArgumentNotValidException argumentNotValidException) {
        return buildErrorResponse(argumentNotValidException);
    }

    private ResponseData buildErrorResponse(Exception ex) {
        log.error(ex.getMessage(), ex);
        ex.printStackTrace();
        return ResponseData.builder()
                .success(false)
                .message(ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage())
                .build();
    }

    private ResponseData buildErrorResponse(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        ex.printStackTrace();
        return ResponseData.builder()
                .success(false)
                .message(MessageFormat.format("Entity '{0}' is not valid", ex.getBindingResult().getObjectName()))
                .build();
    }
}
