package com.vmoscalciuc.parkinglot.services;

import com.vmoscalciuc.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.vmoscalciuc.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.google.zxing.WriterException;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ParkingLotService {
    ParkingLotDtoResponse createParkingLot(ParkingLotDtoRequest parkingLotDtoRequest) throws WriterException;
    ParkingLotDtoResponse updateParkingLot(Long id, ParkingLotDtoRequest parkingLotDtoRequest)  throws WriterException;
    List<ParkingLotDtoResponse> getAllParkingLot(String name);
    ParkingLotDtoResponse getOneParkingLot(Long id);
    void addUser(Long id, Long userId);
    void deleteUserFromParkingLot(Long userId, Long parkingLotId);
    void deleteParkingLot(Long id);
    void performValidationForCreation(ParkingLotDtoRequest parkingLotCreationDtoRequest, BindingResult bindingResult);
    void performValidationForEdit(ParkingLotDtoRequest parkingLotDtoRequest, BindingResult bindingResult, Long id);

}
