package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.parkingLot.LevelDtoForLot;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.endava.parkinglot.enums.SpaceState;
import com.endava.parkinglot.enums.SpaceType;
import com.endava.parkinglot.exceptions.email.FailedEmailNotificationException;
import com.endava.parkinglot.exceptions.parkingLot.NoSuchUserOnParkingLotException;
import com.endava.parkinglot.exceptions.parkingLot.ParkingLotNotFoundException;
import com.endava.parkinglot.exceptions.parkingLot.ParkingSpacesOccupiedException;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.exceptions.user.UserNotGrantedToDoActionException;
import com.endava.parkinglot.mapper.ParkingMapper;
import com.endava.parkinglot.model.*;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import com.endava.parkinglot.model.repository.UserRepository;
import com.endava.parkinglot.model.repository.WorkingDaysRepository;
import com.endava.parkinglot.services.ParkingLotService;
import com.endava.parkinglot.validators.ParkingLotCreationValidator;
import com.endava.parkinglot.validators.ParkingLotEditValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final UserRepository userRepository;
    private final WorkingDaysRepository workingDaysRepository;
    private final ParkingLotCreationValidator parkingLotCreationValidator;
    private final ParkingLotEditValidator parkingLotEditValidator;
    private final ParkingMapper parkingMapper;
    private final EmailNotificationServiceImpl emailNotificationService;

    private static final Logger logger = LoggerFactory.getLogger(ParkingLotServiceImpl.class);

    @Override
    public List<ParkingLotDtoResponse> getAllParkingLot(String searchString) {
        String email = getUsernameOfAuthenticatedUser();

        if (searchString == null) logger.info("Going to look for all the lots for user with email = " + email);
        else logger.info("Going to look for lots with name like: " + searchString);

        List<ParkingLotEntity> lots = parkingLotRepository.search(searchString, email);

        logger.info("Found " + lots.size() + " parking lots based on your query.");

        List<ParkingLotDtoResponse> parkingLotDtoResponses = parkingMapper
                .mapListEntityToListResponseDto(lots);

        addStatistics(parkingLotDtoResponses);

        return parkingLotDtoResponses;
    }


    @Override
    public ParkingLotDtoResponse getOneParkingLot(Long id) {
        logger.info("Going to look for one lot with such id: " + id);
        String email = getUsernameOfAuthenticatedUser();

        if (!parkingLotRepository.checkIfUserExistsOnParkingLotByUserEmail(email, id)){
            logger.error("User with email: " + email + " is not present on parking lot with ID = " + id);
            throw new NoSuchUserOnParkingLotException("User with email: " + email + " is not present on parking lot with ID = " + id);
        }

        ParkingLotDtoResponse response = parkingMapper.mapEntityToResponseDto(
                parkingLotRepository.findById(id)
                        .orElseThrow(() -> {
                            logger.error("There is no lot with id = " + id);
                            return new ParkingLotNotFoundException(id);
                        })
        );

        addStatistics(List.of(response));

        return response;
    }

    @Override
    @Transactional
    public ParkingLotDtoResponse createParkingLot(ParkingLotDtoRequest parkingLotCreationDtoRequest) {
        ParkingLotEntity parkingLot = parkingMapper.mapRequestDtoToEntity(parkingLotCreationDtoRequest);

        addParkingSpacesToLevels(parkingLot);

        ParkingLotEntity savedParkingLot = parkingLotRepository.save(parkingLot);

        UserEntity user = userRepository.findByEmail(getUsernameOfAuthenticatedUser())
                .orElseThrow(() -> new UserNotFoundException("No such username in the system!"));
        savedParkingLot.setUsers(Set.of(user));
        user.getParkingLots().add(savedParkingLot);

        ParkingLotDtoResponse response = parkingMapper.mapEntityToResponseDto(savedParkingLot);

        response.setLevelOfOccupancy(0);
        response.setCountOfAccessibleParkingSpots(0);
        response.setCountOfFamilyFriendlyParkingSpots(0);

        return response;
    }

    @Override
    @Transactional
    public ParkingLotDtoResponse updateParkingLot(Long id, ParkingLotDtoRequest parkingLotDtoRequest){
        logger.info("Looking for parking lot entity with id: " + id + " to update it.");
        ParkingLotEntity parkingLotEntity = parkingLotRepository.findById(id).orElseThrow(
                () -> new UserNotGrantedToDoActionException("User doesn't have authorities to do this action !")
        );

        logger.info("Trying to update parking lot entity with id: " + id + ".");
        parkingMapper.mapTwoEntities(parkingLotEntity, parkingLotDtoRequest);
        reformatWorkingDays(parkingLotEntity, parkingLotDtoRequest);
        reformatLevels(parkingLotEntity, parkingLotDtoRequest);

        logger.info("Reformation of entity was successful");


        parkingLotRepository.save(parkingLotEntity);
        logger.info("Parking Lot was updated successfully");

        ParkingLotDtoResponse response = parkingMapper.mapEntityToResponseDto(parkingLotEntity);

        addStatistics(List.of(response));

        return response;
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

        if (parkingLotRepository.countOfOccupiedParkingSpotsByLotId(id) != 0)
            throw new ParkingSpacesOccupiedException("You can't delete this parking lot because there are occupied parking spots there.");

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
                    "User with ID " + userId + " is not present on parking lot with ID " + parkingLotId);
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

    private void addStatistics(List<ParkingLotDtoResponse> parkingLotDtoResponses) {
        for (ParkingLotDtoResponse response : parkingLotDtoResponses){
            int countOfAllTheSpaces = parkingLotRepository.countOfAllSpaces(response.getId());
            int countOfOccupiedSpaces = parkingLotRepository.countOfOccupiedParkingSpotsByLotId(response.getId());
            response.setLevelOfOccupancy(
                    (countOfOccupiedSpaces * 100) / countOfAllTheSpaces
            );

            response.setCountOfAccessibleParkingSpots(
                    parkingLotRepository.countOfAccessibleParkingSpotsByLotId(response.getId())
            );

            response.setCountOfFamilyFriendlyParkingSpots(
                    parkingLotRepository.countOfFamilyFriendlyParkingSpotsByLotId(response.getId())
            );
        }
    }

    @Override
    public void performValidationForCreation(ParkingLotDtoRequest parkingLotDtoRequest, BindingResult bindingResult) {
        parkingLotCreationValidator.validate(parkingLotDtoRequest, bindingResult);

        parkingLotCreationValidator.bindingResultProcessing(bindingResult);
    }

    @Override
    public void performValidationForEdit(ParkingLotDtoRequest parkingLotDtoRequest, BindingResult bindingResult, Long id) {
        parkingLotEditValidator.validate(parkingLotDtoRequest, bindingResult, id);

        parkingLotEditValidator.bindingResultProcessing(bindingResult);
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

    private void addParkingSpacesToLevels(ParkingLotEntity parkingLotEntity) {
        for(ParkingLevelEntity levelEntity: parkingLotEntity.getLevels()) {
            List<ParkingSpaceEntity> spaces = organizeSpaces(levelEntity);
            levelEntity.setParkingSpaces(spaces);
            levelEntity.addSpacesToLevel(spaces);
        }
    }

    private void reformatLevels(ParkingLotEntity parkingLotEntity, ParkingLotDtoRequest parkingLotDtoRequest) {
        for (LevelDtoForLot dto : parkingLotDtoRequest.getLevels()) {
            ParkingLevelEntity level = containsTheFloorEntity(parkingLotEntity.getLevels(), dto.getFloor());
            int countSpacesDTO = Integer.parseInt(dto.getNumberOfSpaces());

            if (level != null) {
                if (countSpacesDTO > level.getNumberOfSpaces()) {
                    addNewSpacesToExisting(level.getParkingSpaces(), countSpacesDTO, level);
                }
                else if (countSpacesDTO < level.getNumberOfSpaces()) {
                    List<ParkingSpaceEntity> parkingSpaces = level.getParkingSpaces();

                    parkingSpaces.sort((o1, o2) -> (int) (o2.getId() - o1.getId()));

                    int countToRemove = parkingSpaces.size() - countSpacesDTO;

                    int i = 0;
                    while(i < countToRemove){
                        ParkingSpaceEntity spaceEntity = parkingSpaces.get(0);
                        if (spaceEntity.getUser() != null) {
                            logger.error("It is not possible to decrease amount of parking spaces at the floor: '" + level.getFloor() + "', because there are occupied parking spaces!");
                            throw new ParkingSpacesOccupiedException("You can't decrease amount of parking spaces at the floor: '" + level.getFloor() + "', because there are occupied parking spaces!");
                        }
                        parkingSpaces.remove(spaceEntity);
                        i++;
                    }
                }
                level.setNumberOfSpaces(countSpacesDTO);

            }
            else {
                ParkingLevelEntity levelEntity = new ParkingLevelEntity();
                levelEntity.setFloor(dto.getFloor());
                levelEntity.setNumberOfSpaces(countSpacesDTO);
                List<ParkingSpaceEntity> parkingSpaceEntities = organizeSpaces(levelEntity);
                parkingSpaceEntities.forEach(entity -> entity.setParkingLevel(levelEntity));
                levelEntity.setParkingSpaces(parkingSpaceEntities);
                levelEntity.setParkingLot(parkingLotEntity);
                parkingLotEntity.getLevels().add(levelEntity);
            }
        }


        int entitySize = parkingLotEntity.getLevels().size();
        int requestSize = parkingLotDtoRequest.getLevels().size();
        if (entitySize > requestSize) {
            List<ParkingLevelEntity> toDelete = new ArrayList<>();

            for (ParkingLevelEntity level : parkingLotEntity.getLevels()) {
                LevelDtoForLot levelDto = containsTheFloorDTO(parkingLotDtoRequest.getLevels(), level.getFloor());
                if (levelDto == null) {
                    boolean thereAreOccupiedSpaces = level.getParkingSpaces().stream()
                            .anyMatch(space -> space.getUser() != null);
                    if (thereAreOccupiedSpaces) {
                        logger.error("It is not possible to delete the floor: '" + level.getFloor() + "', because there are occupied parking spaces on this floor!");
                        throw new ParkingSpacesOccupiedException("You can't delete the floor: '" + level.getFloor() + "', because there are occupied parking spaces on this floor!");
                    }
                    toDelete.add(level);
                }
            }
            parkingLotEntity.getLevels()
                    .removeIf(toDelete::contains);
        }


    }

    private List<ParkingSpaceEntity> addNewSpacesToExisting(List<ParkingSpaceEntity> parkingSpaces, int endingSize, ParkingLevelEntity parent) {
        int initialSize = parkingSpaces.size();
        for (int i = initialSize + 1; i <= endingSize; i++){
            String formatter = String.format("%03d", i);
            ParkingSpaceEntity newSpace = ParkingSpaceEntity.builder()
                    .number(String.join("-", String.valueOf(parent.getFloor()), formatter))
                    .type(SpaceType.REGULAR)
                    .state(SpaceState.AVAILABLE)
                    .parkingLevel(parent)
                    .build();

            parkingSpaces.add(newSpace);
        }
        return parkingSpaces;
    }

    private ParkingLevelEntity containsTheFloorEntity(List<ParkingLevelEntity> levels, Character floor) {
        for (ParkingLevelEntity entity : levels){
            if (entity.getFloor().equals(floor)){
                return entity;
            }
        }
        return null;
    }

    private LevelDtoForLot containsTheFloorDTO(List<LevelDtoForLot> levels, Character floor) {
        for (LevelDtoForLot request : levels){
            if (request.getFloor().equals(floor)){
                return request;
            }
        }
        return null;
    }

    private void reformatWorkingDays(ParkingLotEntity parkingLotEntity, ParkingLotDtoRequest parkingLotDtoRequest) {
        workingDaysRepository.deleteAllByParkingLotId(parkingLotEntity.getId());
        List<WorkingDaysEntity> days = parkingMapper.stringToWorkinDayList(parkingLotDtoRequest.getWorkingDays());
        days.forEach(day -> day.setParkingLot(parkingLotEntity));
        workingDaysRepository.saveAll(days);
        parkingLotEntity.setWorkingDays(days);
    }
}