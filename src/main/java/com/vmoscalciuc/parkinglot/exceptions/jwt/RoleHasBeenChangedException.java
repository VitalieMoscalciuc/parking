package com.vmoscalciuc.parkinglot.exceptions.jwt;

public class RoleHasBeenChangedException extends RuntimeException {

    public RoleHasBeenChangedException(String message){
        super(message);
    }
}
