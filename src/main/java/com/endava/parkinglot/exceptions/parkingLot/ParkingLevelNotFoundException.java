package com.endava.parkinglot.exceptions.parkingLot;

public class ParkingLevelNotFoundException extends RuntimeException {

    public ParkingLevelNotFoundException(){
        super("This parking level not found in the system!");
    }
}
