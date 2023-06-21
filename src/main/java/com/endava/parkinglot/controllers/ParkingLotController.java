package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.ParkingLotCreationDtoRequest;
import com.endava.parkinglot.DTO.ParkingLotCreationDtoResponse;
import com.endava.parkinglot.services.ParkingLotCreationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parkingLot")
public class ParkingLotController {

    private final ParkingLotCreationService parkingLotCreationService;

    @Autowired
    public ParkingLotController(ParkingLotCreationService parkingLotCreationService) {
        this.parkingLotCreationService = parkingLotCreationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ParkingLotCreationDtoResponse> createParkingLot(
            @Valid @RequestBody ParkingLotCreationDtoRequest parkingLotCreationDtoRequest) {
        ParkingLotCreationDtoResponse newParkingLot
                = parkingLotCreationService.createParkingLot(parkingLotCreationDtoRequest);
        return new ResponseEntity<>(newParkingLot, HttpStatus.CREATED);
    }
}
