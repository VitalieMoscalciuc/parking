package com.endava.parkinglot.exceptions.parkingLot;

public class ParkingSpaceNotFoundException extends RuntimeException {

    public ParkingSpaceNotFoundException(){
        super("There is no spaces based on your request!");
    }
}
