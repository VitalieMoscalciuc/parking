package com.endava.parkinglot.exceptions.parkingLot;

public class ParkingSpacesOccupiedException extends RuntimeException {
    public ParkingSpacesOccupiedException(String message) {
        super(message);
    }
}
