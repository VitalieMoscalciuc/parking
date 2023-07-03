package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.parkingSpace.SpaceDTO;

import java.util.List;

public interface ParkingSpaceService {

    List<SpaceDTO> getAllByLevelId(Long lotId, Long levelId, String name);
}
