package com.example.springtestdemo.exceptions;

public class DataCreationException extends RuntimeException{
    public DataCreationException(String message){
        super(message);
    }
}
