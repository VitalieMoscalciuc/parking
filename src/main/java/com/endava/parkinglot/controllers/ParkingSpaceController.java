package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.parkingSpace.SpaceDTO;
import com.endava.parkinglot.enums.SpaceType;
import com.endava.parkinglot.services.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parkingSpace")
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<SpaceDTO>> getAllByLevelId(@RequestParam("lot_id") Long lotId,
                                                          @RequestParam("level_id") Long levelId,
                                                          @RequestParam(name = "name", required = false) String searchString) {
        List<SpaceDTO> spaces = parkingSpaceService.getAllByLevelId(lotId, levelId, searchString);

        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    @PutMapping("/edit/byAdmin/{id}")
    public ResponseEntity<HttpStatus> editParkingSpaceType(@PathVariable("id") Long spaceId, @RequestParam("type") SpaceType spaceType) {

        parkingSpaceService.editParkingSpaceType(spaceId, spaceType);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/{parkingSpaceId}/addUser")
    public ResponseEntity<HttpStatus> addUserToParkingSpot(@PathVariable Long parkingSpaceId,
                                                           @RequestParam("userId") Long userId)

    {
       // parkingSpaceService.addUser(parkingSpaceId,userId);
       
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
