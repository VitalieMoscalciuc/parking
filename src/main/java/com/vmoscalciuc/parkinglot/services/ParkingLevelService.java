package com.vmoscalciuc.parkinglot.services;

import com.vmoscalciuc.parkinglot.DTO.parkingLevel.LevelDTO;

import java.util.List;

public interface ParkingLevelService {

    List<LevelDTO> getAllByLotId(Long id);
}
