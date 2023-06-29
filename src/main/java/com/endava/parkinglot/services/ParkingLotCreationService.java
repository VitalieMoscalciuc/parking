package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.ParkingLotCreationDtoRequest;
import com.endava.parkinglot.DTO.ParkingLotCreationDtoResponse;

import java.util.List;

public interface ParkingLotCreationService {
    ParkingLotCreationDtoResponse createParkingLot(ParkingLotCreationDtoRequest parkingLotCreationDtoRequest);
<<<<<<< src/main/java/com/endava/parkinglot/services/ParkingLotCreationService.java
    List<ParkingLotCreationDtoResponse> getAllParkingLot(String name);
    void addUser(int id, Long userId);
=======
    List<ParkingLotCreationDtoResponse> getAllParkingLot(String name);
>>>>>>> src/main/java/com/endava/parkinglot/services/ParkingLotCreationService.java
}
