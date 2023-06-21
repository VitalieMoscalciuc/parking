package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.ParkingLotCreationDtoRequest;
import com.endava.parkinglot.DTO.ParkingLotCreationDtoResponse;

public interface ParkingLotCreationService {
    ParkingLotCreationDtoResponse createParkingLot(ParkingLotCreationDtoRequest parkingLotCreationDtoRequest);
}
