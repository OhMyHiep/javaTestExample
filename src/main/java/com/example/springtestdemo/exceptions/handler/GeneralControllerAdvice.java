package com.example.springtestdemo.exceptions.handler;

import com.example.springtestdemo.domain.response.ServiceStatus;
import com.example.springtestdemo.exceptions.DataCreationException;
import com.example.springtestdemo.exceptions.DataNotFoundException;
import com.example.springtestdemo.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralControllerAdvice {
    Logger log = LoggerFactory.getLogger(this.getClass());

    //for example, pathvariable type mismatch, or didnt provide request body
    @ExceptionHandler(value = {TypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ServiceStatus> handlerNumberFormatException(Exception e) {
        return new ResponseEntity(ServiceStatus.builder().success(false).errorMessage("User Input Error. Please check on API documentation to make sure your request follow the desired pattern.").build(), HttpStatus.BAD_REQUEST);
    }

    //for example, failed on request body validation or failed to create data
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, DataCreationException.class})
    public ResponseEntity<ServiceStatus> handlerInvalidRequestBody(Exception e) {
        return new ResponseEntity(ServiceStatus.builder().success(false).errorMessage(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<ServiceStatus> handlerDataNotFound(RuntimeException e) {
        return new ResponseEntity(ServiceStatus.builder().success(false).errorMessage(e.getMessage()).build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<ServiceStatus> handlerInvalidTokenException(InvalidTokenException e) {
        return new ResponseEntity(ServiceStatus.builder().success(false).errorMessage(e.getMessage()).build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ServiceStatus> handlerException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity(ServiceStatus.builder().success(false).errorMessage(e.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

}