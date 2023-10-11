package com.vmoscalciuc.parkinglot.validators;

import com.vmoscalciuc.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.vmoscalciuc.parkinglot.model.ParkingLotEntity;
import com.vmoscalciuc.parkinglot.model.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ParkingLotCreationValidator extends ParkingLotGeneralValidator {

    private final ParkingLotRepository parkingLotRepository;

    public void validate(Object target, Errors errors) {
        ParkingLotDtoRequest dtoRequest = generalValidation(target, errors);

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
}