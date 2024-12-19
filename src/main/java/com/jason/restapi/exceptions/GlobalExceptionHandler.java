package com.jason.restapi.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.jason.restapi.exceptions.ResourceNotFoundException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jason.restapi.io.ErrorObject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for Spring application
 * @author Jason
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorObject handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Throwing ResourceNotFoundException from Global Exception Handler {}", ex.getMessage());
        return ErrorObject.builder()
            .errorCode("DATA_NOT_FOUND")
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .timestamp(new Date())
            .build();
    }
}
