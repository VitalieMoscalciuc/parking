package com.endava.parkinglot.util;

import com.endava.parkinglot.DTO.parkingLot.LevelDtoForLot;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class ParkingLotValidator implements Validator {

    private final ParkingLotRepository parkingLotRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ParkingLotDtoRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ParkingLotDtoRequest dtoRequest = (ParkingLotDtoRequest) target;

        if (dtoRequest.getWorkingDays() != null) {
            for (String day : dtoRequest.getWorkingDays()) {
                if (!validateTheDay(day)) {
                    errors.rejectValue("workingDays", "", "day you noticed: '" + day + "' is invalid !");
                }
            }
        }

        if (dtoRequest.getLevels() != null) {
            Set<String> duplicateChecking = new HashSet<>();
            for (LevelDtoForLot levelDtoForLot : dtoRequest.getLevels()) {
                if (levelDtoForLot.getFloor() != null) {
                    if (!Pattern.matches("^[A-Z]$", Character.toString(levelDtoForLot.getFloor()))) {
                        errors.rejectValue("levels", "", "Level' floor can be only single alphabetical character! " +
                                "Character you typed for floor: '" + levelDtoForLot.getFloor() + "' is invalid!");
                    }
                    if (duplicateChecking.contains(String.valueOf(levelDtoForLot.getFloor()))) {
                        errors.rejectValue("levels", "", "You try to add duplicate floor: '" +
                                levelDtoForLot.getFloor() + "'.");
                    }
                    duplicateChecking.add(String.valueOf(levelDtoForLot.getFloor()));
                }
            }
        }

        if (dtoRequest.getName() != null) {
            Optional<ParkingLotEntity> nameCheck = parkingLotRepository.findByName(dtoRequest.getName());
            if (nameCheck.isPresent()) {
                errors.rejectValue("name", "", "The parkingLot with this name is already registered in the system !");
            }
        }

        if ( dtoRequest.getAddress() != null) {
            Optional<ParkingLotEntity> addressCheck = parkingLotRepository.findByAddress(dtoRequest.getAddress());
            if (addressCheck.isPresent()) {
                errors.rejectValue("address", "", "The parkingLot with this address is already registered in the system !");
            }
        }
    }


    private boolean validateTheDay(String day) {
        return day.equals("MONDAY") || day.equals("TUESDAY") || day.equals("WEDNESDAY") || day.equals("THURSDAY")
                || day.equals("FRIDAY") || day.equals("SATURDAY") || day.equals("SUNDAY");
    }
}