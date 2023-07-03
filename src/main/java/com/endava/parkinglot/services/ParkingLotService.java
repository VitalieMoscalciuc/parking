package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;

import java.util.List;

public interface ParkingLotService {
    ParkingLotDtoResponse createParkingLot(ParkingLotDtoRequest parkingLotDtoRequest);
    List<ParkingLotDtoResponse> getAllParkingLot(String name);
    ParkingLotDtoResponse getOneParkingLot(Long id);
    //Map<String, List<SpaceDTO>> getAllLevelsAndSpaces(Long id, String name);

    void addUser(Long id, Long userId);

    void deleteParkingLot(Long id);
}
