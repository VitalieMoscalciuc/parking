package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.parkingSpace.SpaceDTO;
import com.endava.parkinglot.enums.SpaceType;
import com.endava.parkinglot.exceptions.parkingLot.ParkingSpaceNotFoundException;
import com.endava.parkinglot.mapper.ParkingSpaceMapper;
import com.endava.parkinglot.model.ParkingSpaceEntity;
import com.endava.parkinglot.model.repository.ParkingSpaceRepository;
import com.endava.parkinglot.services.ParkingSpaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceMapper spaceMapper;
    private static final Logger logger = LoggerFactory.getLogger(ParkingSpaceServiceImpl.class);


    @Autowired
    public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, ParkingSpaceMapper spaceMapper) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.spaceMapper = spaceMapper;
    }


    @Override
    public List<SpaceDTO> getAllByLevelId(Long lotId, Long levelId, String searchString) {
        logger.info("Trying to get all the spaces from specific parking lot: " + lotId + " and from specific level: " + levelId);
        List<ParkingSpaceEntity> spaces = parkingSpaceRepository.getAllByParkingLevelId(lotId, levelId, searchString);
        if (spaces.isEmpty()){
            logger.error("There is no spaces based on your request ! Parking Lot id: " + lotId + ", level id: "
                    + levelId + ", searchString: " + searchString);
            throw new ParkingSpaceNotFoundException();
        }

        return spaceMapper.mapEntityListToDTOlist(spaces);
    }

    @Override
    public void editParkingSpaceType(Long spaceId, SpaceType spaceType) {

    }
}
