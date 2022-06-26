package com.example.springtestdemo.exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(){
        super("Invalid User Token");
    }
}
