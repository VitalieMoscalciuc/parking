package com.vmoscalciuc.parkinglot.exceptions.parkingLot;

public class NoSuchUserOnParkingLotException extends RuntimeException {
    public NoSuchUserOnParkingLotException(String message){
        super(message);
    }
}

