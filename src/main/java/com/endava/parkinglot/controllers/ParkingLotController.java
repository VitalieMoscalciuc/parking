package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.endava.parkinglot.exceptions.validation.ValidationCustomException;
import com.endava.parkinglot.services.ParkingLotService;
import com.endava.parkinglot.util.ParkingLotValidator;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/parkingLot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;
    private final ParkingLotValidator parkingLotValidator;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService, ParkingLotValidator parkingLotValidator) {
        this.parkingLotService = parkingLotService;
        this.parkingLotValidator = parkingLotValidator;
    }

    @GetMapping("/get")
    public ResponseEntity<List<ParkingLotDtoResponse>> getAll(
            @RequestParam(required = false) String searchString) {
        List<ParkingLotDtoResponse> parkingLots
                = parkingLotService.getAllParkingLot(searchString);
        return new ResponseEntity<>(parkingLots, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ParkingLotDtoResponse> getOne(@PathVariable("id") Long id){
        ParkingLotDtoResponse response = parkingLotService.getOneParkingLot(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ParkingLotDtoResponse> createParkingLot(
            @Valid @RequestBody ParkingLotDtoRequest parkingLotCreationDtoRequest, BindingResult bindingResult) {

        parkingLotValidator.validate(parkingLotCreationDtoRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                if (!errors.containsKey(error.getField())) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            }

            throw new ValidationCustomException(errors);
        }

        ParkingLotDtoResponse newParkingLot
                = parkingLotService.createParkingLot(parkingLotCreationDtoRequest);

        return new ResponseEntity<>(newParkingLot, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/addUser")
    public ResponseEntity<HttpStatus> addUserToParkingLot(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
        parkingLotService.addUser(id, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deleteUser")
    public ResponseEntity<Map<String, Object>> deleteParkingLot(@PathVariable("id") Long id) {
        parkingLotService.deleteParkingLot(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{parkingLotId}/deleteUser")
    public ResponseEntity<HttpStatus> deleteUserFromParkingLot(@PathVariable Long parkingLotId
            ,@RequestParam(value = "userId") Long userId) {
        parkingLotService.deleteUserFromParkingLot(userId, parkingLotId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}