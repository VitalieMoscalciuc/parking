package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;

import java.util.List;

public interface ParkingLevelService {

    List<LevelDTO> getAllByLotId(Long id);
}
