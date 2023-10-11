package com.vmoscalciuc.parkinglot.DTO.parkingSpace;

import com.vmoscalciuc.parkinglot.enums.SpaceState;
import com.vmoscalciuc.parkinglot.enums.SpaceType;
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
