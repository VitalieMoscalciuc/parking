package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.parkingSpace.SpaceDTO;
import com.endava.parkinglot.enums.SpaceState;
import com.endava.parkinglot.enums.SpaceType;
import com.endava.parkinglot.exceptions.parkingLot.ParkingSpaceNotFoundException;
import com.endava.parkinglot.exceptions.parkingLot.ParkingSpacesOccupiedException;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.mapper.ParkingSpaceMapper;
import com.endava.parkinglot.model.ParkingSpaceEntity;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.ParkingSpaceRepository;
import com.endava.parkinglot.model.repository.UserRepository;
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
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(ParkingSpaceServiceImpl.class);


    @Autowired
    public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, ParkingSpaceMapper spaceMapper
            ,UserRepository userRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.spaceMapper = spaceMapper;
        this.userRepository = userRepository;
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

    @Override
    public void addUserToParkingSpace(Long spaceId, Long userId) {
        logger.info("adding user to parking space");
        ParkingSpaceEntity parkingSpace = parkingSpaceRepository
                .findById(spaceId)
                .orElseThrow(
                        ParkingSpaceNotFoundException::new
                );

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with Id "+userId+" is not found in system")
        );
        if (SpaceState.OCCUPIED.equals(parkingSpace.getState())) {
            logger.error("Parking Space with ID {} is already occupied", spaceId);
            throw new ParkingSpacesOccupiedException("Parking space is already occupied");
        }else{
            parkingSpace.setState(SpaceState.OCCUPIED);
            parkingSpace.setUser(userEntity);
            parkingSpaceRepository.save(parkingSpace);
            logger.info("User is added to parking space");
        }
    }
}
