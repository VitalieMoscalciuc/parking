package com.endava.parkinglot.exceptions.validation;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(){
        super("Too Many Requests, try again after an hour");
    }
}
