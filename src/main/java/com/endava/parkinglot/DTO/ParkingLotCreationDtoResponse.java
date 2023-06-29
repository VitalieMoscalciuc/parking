package com.endava.parkinglot.DTO;

import java.util.List;
import java.util.Set;

public class ParkingLotCreationDtoResponse {
    private String name;
    private String address;
    private String workingHours;
    private Set<String> workingDays;
    private Boolean isClosed;
    private Boolean operatesNonStop;
    private List<LevelDto> levels;
}
