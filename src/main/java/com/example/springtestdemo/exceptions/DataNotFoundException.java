package com.example.springtestdemo.exceptions;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message){
        super(message);
    }
}
