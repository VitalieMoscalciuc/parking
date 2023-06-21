package com.endava.parkinglot.exceptions;

public class UserNotGrantedToDoActionException extends RuntimeException {
    public UserNotGrantedToDoActionException(String message) {
        super(message);
    }
}
