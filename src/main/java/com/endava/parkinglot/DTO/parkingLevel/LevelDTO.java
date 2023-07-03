package com.endava.parkinglot.DTO.parkingLevel;

public class LevelDTO {

    private Long id;
    private Character floor;
    private Integer numberOfSpaces;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Character getFloor() {
        return floor;
    }

    public void setFloor(Character floor) {
        this.floor = floor;
    }

    public Integer getNumberOfSpaces() {
        return numberOfSpaces;
    }

    public void setNumberOfSpaces(Integer numberOfSpaces) {
        this.numberOfSpaces = numberOfSpaces;
    }
}
