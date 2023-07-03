package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.endava.parkinglot.exceptions.email.FailedEmailNotificationException;
import com.endava.parkinglot.exceptions.parkingLot.NoSuchUserOnParkingLotException;
import com.endava.parkinglot.exceptions.parkingLot.ParkingLotNotFoundException;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.mapper.ParkingMapper;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import com.endava.parkinglot.model.repository.UserRepository;
import com.endava.parkinglot.services.ParkingLotService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingMapper parkingMapper;
    private final UserRepository userRepository;
    private final EmailNotificationServiceImpl emailNotificationService;
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository, ParkingMapper parkingMapper, UserRepository userRepository, EmailNotificationServiceImpl emailNotificationService) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingMapper = parkingMapper;

        this.userRepository = userRepository;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public List<ParkingLotDtoResponse> getAllParkingLot(String searchString) {
        if (searchString == null) logger.info("Going to look for all the lots");
        else logger.info("Going to look for lots with name like: " + searchString);

        return parkingMapper
                .mapListEntityToListResponseDto(
                        parkingLotRepository.search(searchString)
                );
    }

    @Override
    public ParkingLotDtoResponse getOneParkingLot(Long id) {
        logger.info("Going to look for one lot with such id: " + id);
        return parkingMapper.mapEntityToResponseDto(
                parkingLotRepository.findById(id)
                        .orElseThrow(() -> {
                            logger.error("There is no lot with id = " + id);
                            return new ParkingLotNotFoundException(id);
                        })
        );
    }

//    @Override
//    public Map<String, List<SpaceDTO>> getAllLevelsAndSpaces(Long id, String name){
//        ParkingLotEntity entity = parkingLotRepository.findById(id)
//                .orElseThrow(ParkingLotNotFoundException::new);
//
//        List<LevelDTO> levels = levelMapper.fromEntityListToDTOList(entity.getLevels());
//        Map<String, List<SpaceDTO>> response = new HashMap<>();
//
//        for (LevelDTO level : levels){
//            response.put(String.valueOf(level.getFloor()), level.getSpots());
//        }
//
//        return response;
//    }

    @Override
    public ParkingLotDtoResponse createParkingLot(ParkingLotDtoRequest parkingLotDtoRequest) {
        return null;
    }

    @Override
    @Transactional
    public void addUser(Long id, Long userId) {
        ParkingLotEntity parkingLot = parkingLotRepository.findById(id).orElseThrow(
                () -> new ParkingLotNotFoundException(id)
        );

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with ID " + userId + " not found.")
        );

        parkingLot.getUsers().add(user);
        user.getParkingLots().add(parkingLot);

        parkingLotRepository.save(parkingLot);
        userRepository.save(user);

        try {
            emailNotificationService.sendNotificationAboutAddedToParkingLot(user.getEmail(), parkingLot.getName());
        } catch (FailedEmailNotificationException failedEmailNotificationException) {
            logger.warn("Email was not sent, user not added to parking lot: " + user.getEmail());
        }
    }

    @Override
    public void deleteParkingLot(Long id) {
        if (!parkingLotRepository.existsById(id)) {
            throw new ParkingLotNotFoundException(id);
        }

        parkingLotRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteUserFromParkingLot(Long userId, Long parkingLotId) {
        ParkingLotEntity parkingLot = parkingLotRepository
                .findById(parkingLotId).orElseThrow
                        (() -> new ParkingLotNotFoundException(parkingLotId));

        UserEntity user = userRepository
                .findById(userId).orElseThrow
                        (() -> new UserNotFoundException("User with ID " + userId + " not found."));

        if(!parkingLotRepository
                .checkIfUserExistsOnParkingLot(userId,parkingLotId)){
            throw new NoSuchUserOnParkingLotException(
                    "User with ID " + userId + " is not present on parking lot with ID "+parkingLotId);
        }

        logger.info("Removing User with ID "+ userId + " from parking lot with ID "+ parkingLotId);

        parkingLotRepository
                .removeUserFromParkingLot(userId, parkingLotId);

        try {
            emailNotificationService
                    .sendNotificationAboutDeletionFromParkingLot(user.getEmail(), parkingLot.getName());
        } catch (FailedEmailNotificationException exception) {
            logger.warn("Email was not sent, user not added to parking lot: {}", user.getEmail());
        }
    }
}

