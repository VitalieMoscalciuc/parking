package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;
import com.endava.parkinglot.services.ParkingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parkingLevel")
public class ParkingLevelController {

    private final ParkingLevelService parkingLevelService;

    @Autowired
    public ParkingLevelController(ParkingLevelService parkingLevelService) {
        this.parkingLevelService = parkingLevelService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<LevelDTO>> getLevelsByLotId(@RequestParam(name = "lot_id") Long id){
        List<LevelDTO> levels = parkingLevelService.getAllByLotId(id);

        return new ResponseEntity<>(levels, HttpStatus.OK);
    }
}
