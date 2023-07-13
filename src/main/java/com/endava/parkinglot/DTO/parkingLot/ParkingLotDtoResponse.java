package com.endava.parkinglot.DTO.parkingLot;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;
import com.endava.parkinglot.enums.WorkingDays;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParkingLotDtoResponse {

    private Long id;
    private String name;
    private String address;
    private String workingHours;
    private List<WorkingDays> workingDays;
    private List<LevelDTO> levels;
    private Boolean isClosed;
    private Boolean operatesNonStop;
    private Integer levelOfOccupancy;
    private Integer countOfAccessibleParkingSpots;
    private Integer countOfFamilyFriendlyParkingSpots;
}
