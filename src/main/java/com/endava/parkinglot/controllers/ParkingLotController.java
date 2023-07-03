package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.endava.parkinglot.services.ParkingLotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parkingLot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
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

//    @GetMapping("/get/{id}/spots")
//    public ResponseEntity<Map<String, List<SpaceDTO>>> getSpots(@PathVariable("id") Long id,
//                           @RequestParam(name = "name", required = false) String name){
//        Map<String, List<SpaceDTO>> spaces = parkingLotService.getAllLevelsAndSpaces(id, name);
//
//        return new ResponseEntity<>(spaces, HttpStatus.OK);
//    }

    @PostMapping("/create")
    public ResponseEntity<ParkingLotDtoResponse> createParkingLot(
            @Valid @RequestBody ParkingLotDtoRequest parkingLotDtoRequest) {
        ParkingLotDtoResponse newParkingLot
                = parkingLotService.createParkingLot(parkingLotDtoRequest);
        return new ResponseEntity<>(newParkingLot, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/addUser")
    public ResponseEntity<HttpStatus> addUserToParkingLot(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
        parkingLotService.addUser(id, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
