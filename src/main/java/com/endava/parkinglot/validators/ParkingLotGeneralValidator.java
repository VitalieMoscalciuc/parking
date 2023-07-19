package com.endava.parkinglot.validators;

import com.endava.parkinglot.DTO.parkingLot.LevelDtoForLot;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.exceptions.validation.ValidationCustomException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ParkingLotGeneralValidator {

    static public ParkingLotDtoRequest generalValidation(Object target, Errors errors) {
        ParkingLotDtoRequest dtoRequest = (ParkingLotDtoRequest) target;

        Set<String> workingDays = dtoRequest.getWorkingDays();

        if (workingDays != null) {
            if (workingDays.size() == 0 && !dtoRequest.isOperatesNonStop()){
                errors.rejectValue("workingDays", "", "At least one day of the week must be selected");
            }
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
                    if (!Pattern.matches("^[A-Z]$", levelDtoForLot.getFloor())) {
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

        return dtoRequest;
    }

    public void bindingResultProcessing(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                if (!errors.containsKey(error.getField())) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            }

            throw new ValidationCustomException(errors);
        }
    }


    static private boolean validateTheDay(String day) {
        return day.equals("MONDAY") || day.equals("TUESDAY") || day.equals("WEDNESDAY") || day.equals("THURSDAY")
                || day.equals("FRIDAY") || day.equals("SATURDAY") || day.equals("SUNDAY");
    }
}
