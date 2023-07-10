package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.endava.parkinglot.enums.SpaceState;
import com.endava.parkinglot.enums.SpaceType;
import com.endava.parkinglot.exceptions.email.FailedEmailNotificationException;
import com.endava.parkinglot.exceptions.parkingLot.NoSuchUserOnParkingLotException;
import com.endava.parkinglot.exceptions.parkingLot.ParkingLotNotFoundException;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.mapper.ParkingMapper;
import com.endava.parkinglot.model.ParkingLevelEntity;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.ParkingSpaceEntity;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import com.endava.parkinglot.model.repository.UserRepository;
import com.endava.parkinglot.services.ParkingLotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingMapper parkingMapper;
    private final UserRepository userRepository;
    private final EmailNotificationServiceImpl emailNotificationService;
    private static final Logger logger = LoggerFactory.getLogger(ParkingLotServiceImpl.class);

    @Override
    public List<ParkingLotDtoResponse> getAllParkingLot(String searchString) {
        String email = getUsernameOfAuthenticatedUser();

        if (searchString == null) logger.info("Going to look for all the lots for user with email = " + email);
        else logger.info("Going to look for lots with name like: " + searchString);

        List<ParkingLotEntity> lots = parkingLotRepository.search(searchString, email);

        logger.info("Found " + lots.size() + " parking lots based on your query.");

        return parkingMapper
                .mapListEntityToListResponseDto(lots);
    }

    @Override
    public ParkingLotDtoResponse getOneParkingLot(Long id) {
        logger.info("Going to look for one lot with such id: " + id);
        String email = getUsernameOfAuthenticatedUser();

        if (!parkingLotRepository.checkIfUserExistsOnParkingLotByUserEmail(email, id)){
            logger.error("User with email: " + email + " is not present on parking lot with ID = " + id);
            throw new NoSuchUserOnParkingLotException("User with email: " + email + " is not present on parking lot with ID = " + id);
        }

        return parkingMapper.mapEntityToResponseDto(
                parkingLotRepository.findById(id)
                        .orElseThrow(() -> {
                            logger.error("There is no lot with id = " + id);
                            return new ParkingLotNotFoundException(id);
                        })
        );
    }

    @Override
    @Transactional
    public ParkingLotDtoResponse createParkingLot(ParkingLotDtoRequest parkingLotCreationDtoRequest) {
        ParkingLotEntity parkingLot = parkingMapper.mapRequestDtoToEntity(parkingLotCreationDtoRequest);

        for(ParkingLevelEntity levelEntity: parkingLot.getLevels()) {
            List<ParkingSpaceEntity> spaces = organizeSpaces(levelEntity);
            levelEntity.setParkingSpaces(spaces);
            levelEntity.addSpacesToLevel(spaces);
        }

        ParkingLotEntity savedParkingLot = parkingLotRepository.save(parkingLot);

        return parkingMapper.mapEntityToResponseDto(savedParkingLot);
    }


    @Override
    @Transactional
    public void addUser(Long id, Long userId) {
        logger.info("Begin to add user in the parking lot");

        ParkingLotEntity parkingLot = parkingLotRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Fail to add user in Parking Lot. Parking Lot with id = " + id + " not found!");
                    return new ParkingLotNotFoundException(id);
                }
        );

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Fail to add user in Parking Lot. User with id = " + userId + " not found!");
                    return new UserNotFoundException("User with ID " + userId + " not found.");
                }
        );

        logger.info("Going to add user with id = " + userId + " in parking lot with id = " + id);

        parkingLot.getUsers().add(user);
        user.getParkingLots().add(parkingLot);

        parkingLotRepository.save(parkingLot);
        userRepository.save(user);

        logger.info("User is added to the parking lot");

        try {
            emailNotificationService.sendNotificationAboutAddedToParkingLot(user.getEmail(), parkingLot.getName());
        } catch (FailedEmailNotificationException failedEmailNotificationException) {
            logger.warn("Email was not sent, user not added to parking lot: " + user.getEmail());
        }
    }

    @Override
    public void deleteParkingLot(Long id) {
        logger.info("Trying to delete parking lot from system");

        if (!parkingLotRepository.existsById(id)) {
            logger.error("There is no parking lot with id = " + id);
            throw new ParkingLotNotFoundException(id);
        }

        parkingLotRepository.deleteById(id);
        logger.info("Parking lot with id = " + id + " was successfully deleted.");
    }

    @Transactional
    @Override
    public void deleteUserFromParkingLot(Long userId, Long parkingLotId) {
        ParkingLotEntity parkingLot = parkingLotRepository
                .findById(parkingLotId)
                    .orElseThrow(() -> {
                        logger.error("Fail to add user in Parking Lot. " +
                                "Parking Lot with id = " + parkingLotId + " not found!");
                        return new ParkingLotNotFoundException(parkingLotId);
                    });

        UserEntity user = userRepository
                .findById(userId)
                    .orElseThrow(() -> {
                        logger.error("Fail to add user in Parking Lot. User with id = " + userId + " not found!");
                        return new UserNotFoundException("User with ID " + userId + " not found.");
                    });

        if(!parkingLotRepository
                .checkIfUserExistsOnParkingLotByUserId(userId,parkingLotId)){
            logger.error("User you try to delete from parking lot with id = "
                    + parkingLotId + " was not originally added in this parking lot.");
            throw new NoSuchUserOnParkingLotException(
                    "User with ID " + userId + " is not present on parking lot with ID "+parkingLotId);
        }

        logger.info("Removing User with ID "+ userId + " from parking lot with ID "+ parkingLotId);

        parkingLotRepository
                .removeUserFromParkingLot(userId, parkingLotId);

        logger.info("User was successfully deleted from parking lot.");

        try {
            emailNotificationService
                    .sendNotificationAboutDeletionFromParkingLot(user.getEmail(), parkingLot.getName());
        } catch (FailedEmailNotificationException exception) {
            logger.warn("Email was not sent, user not added to parking lot: {}", user.getEmail());
        }
    }

    private String getUsernameOfAuthenticatedUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userDetails.getUsername();
    }


    private List<ParkingSpaceEntity> organizeSpaces(ParkingLevelEntity levelEntity) {
        List<ParkingSpaceEntity> spaces = new ArrayList<>();

        int numberOfSpaces = levelEntity.getNumberOfSpaces();
        for (int i = 1; i <= numberOfSpaces; i++) {
            String var = String.format("%03d", i);
            ParkingSpaceEntity space = ParkingSpaceEntity.builder()
                    .number(String.join("-", String.valueOf(levelEntity.getFloor()), var))
                    .type(SpaceType.REGULAR)
                    .state(SpaceState.AVAILABLE)
                    .build();
            spaces.add(space);
        }
        return spaces;
    }

}