package com.endava.parkinglot.util;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

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

        Optional<ParkingLotEntity> nameCheck = parkingLotRepository.findByName(dtoRequest.getName());

        if (nameCheck.isPresent()) {
            errors.rejectValue("name", "", "The parkingLot with this name is already registered in the system !");
        }
    }
}