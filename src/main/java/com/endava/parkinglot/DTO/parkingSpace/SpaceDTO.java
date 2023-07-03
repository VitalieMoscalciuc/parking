package com.endava.parkinglot.DTO.parkingSpace;

import com.endava.parkinglot.enums.SpaceState;
import com.endava.parkinglot.enums.SpaceType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceDTO {

    private String number;

    private SpaceType type;

    private SpaceState state;

}
