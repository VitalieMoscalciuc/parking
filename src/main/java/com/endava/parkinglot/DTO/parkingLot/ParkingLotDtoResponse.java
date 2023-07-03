package com.endava.parkinglot.DTO.parkingLot;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;
import com.endava.parkinglot.enums.WorkingDays;

import java.util.List;

public class ParkingLotDtoResponse {

    private Long id;
    private String name;
    private String address;
    private String workingHours;
    private List<WorkingDays> workingDays;
    private List<LevelDTO> levels;
    private Boolean isClosed;
    private Boolean operatesNonStop;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LevelDTO> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelDTO> levels) {
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public List<WorkingDays> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<WorkingDays> workingDays) {
        this.workingDays = workingDays;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public Boolean getOperatesNonStop() {
        return operatesNonStop;
    }

    public void setOperatesNonStop(Boolean operatesNonStop) {
        this.operatesNonStop = operatesNonStop;
    }
}
