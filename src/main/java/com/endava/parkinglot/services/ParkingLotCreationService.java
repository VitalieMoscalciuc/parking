package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.ParkingLotCreationDtoRequest;
import com.endava.parkinglot.DTO.ParkingLotCreationDtoResponse;

import java.util.List;

public interface ParkingLotCreationService {
    ParkingLotCreationDtoResponse createParkingLot(ParkingLotCreationDtoRequest parkingLotCreationDtoRequest);
    List<ParkingLotCreationDtoResponse> getAllParkingLot();
    void addUser(int id, Long userId);
}
