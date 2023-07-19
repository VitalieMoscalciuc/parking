package com.endava.parkinglot.DTO.parkingLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelDTO {

    private Long id;
    private String floor;
    private Integer numberOfSpaces;
}
