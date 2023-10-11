package com.vmoscalciuc.parkinglot.services;

import com.vmoscalciuc.parkinglot.DTO.parkingSpace.SpaceDTO;
import com.vmoscalciuc.parkinglot.enums.SpaceType;

import java.util.List;

public interface ParkingSpaceService {

    List<SpaceDTO> getAllByLevelId(Long lotId, Long levelId, String name);

    void editParkingSpaceType(Long spaceId, SpaceType spaceType);

    void addUserToParkingSpace(Long spaceId,Long userId);
}
