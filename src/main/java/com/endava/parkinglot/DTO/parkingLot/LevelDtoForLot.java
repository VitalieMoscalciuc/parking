package com.endava.parkinglot.DTO.parkingLot;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelDtoForLot {

    private char floor;

    @Pattern(regexp = "^(?:[1-9]|[1-9][0-9]|1[0-4][0-9]|150)$",
            message = "The field should accept digits only, different than zero and maxim 150 spots")
    private String numberOfSpaces;

}
