package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.parkingSpace.SpaceDTO;
import com.endava.parkinglot.enums.SpaceType;

import java.util.List;

public interface ParkingSpaceService {

    List<SpaceDTO> getAllByLevelId(Long lotId, Long levelId, String name);

    void editParkingSpaceType(Long spaceId, SpaceType spaceType);

    void addUserToParkingSpace(Long spaceId,Long userId);
}
