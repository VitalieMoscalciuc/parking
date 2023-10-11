package com.vmoscalciuc.parkinglot.exceptions.parkingLot;

public class ParkingLotNotFoundException extends RuntimeException {

    public ParkingLotNotFoundException(Long id) {
        super("Parking lot with such ID(" + id + ") not found in the system !");
    }
}
