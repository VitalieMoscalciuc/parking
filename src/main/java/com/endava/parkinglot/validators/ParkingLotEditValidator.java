package com.endava.parkinglot.validators;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ParkingLotEditValidator extends ParkingLotGeneralValidator {

    private final ParkingLotRepository parkingLotRepository;

    public void validate(Object target, Errors errors, Long currentId){
        ParkingLotDtoRequest dtoRequest = ParkingLotGeneralValidator.generalValidation(target, errors);

        if (dtoRequest.getName() != null) {
            Optional<ParkingLotEntity> nameCheck = parkingLotRepository.findByNameExceptSelf(dtoRequest.getName(), currentId);
            if (nameCheck.isPresent()) {
                errors.rejectValue("name", "", "The parkingLot with this name is already registered in the system !");
            }
        }

        if ( dtoRequest.getAddress() != null) {
            Optional<ParkingLotEntity> addressCheck = parkingLotRepository.findByAddressExceptSelf(dtoRequest.getAddress(), currentId);
            if (addressCheck.isPresent()) {
                errors.rejectValue("address", "", "The parkingLot with this address is already registered in the system !");
            }
        }
    }
}
