package com.vmoscalciuc.parkinglot.exceptions.user;

public class UserNotGrantedToDoActionException extends RuntimeException {
    public UserNotGrantedToDoActionException(String message) {
        super(message);
    }
}
