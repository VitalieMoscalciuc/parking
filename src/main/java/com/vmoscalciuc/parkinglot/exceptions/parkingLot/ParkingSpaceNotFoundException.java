package com.vmoscalciuc.parkinglot.exceptions.parkingLot;

public class ParkingSpaceNotFoundException extends RuntimeException {

    public ParkingSpaceNotFoundException(){
        super("There is no spaces based on your request!");
    }
}
