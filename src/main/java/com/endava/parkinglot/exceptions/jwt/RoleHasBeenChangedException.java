package com.endava.parkinglot.exceptions.jwt;

public class RoleHasBeenChangedException extends RuntimeException {

    public RoleHasBeenChangedException(String message){
        super(message);
    }
}
