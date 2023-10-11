package com.vmoscalciuc.parkinglot.mapper;

import com.vmoscalciuc.parkinglot.DTO.parkingLevel.LevelDTO;
import com.vmoscalciuc.parkinglot.model.ParkingLevelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParkingLevelMapper {

    private final ParkingSpaceMapper spaceMapper;

    @Autowired
    public ParkingLevelMapper(ParkingSpaceMapper spaceMapper) {
        this.spaceMapper = spaceMapper;
    }

    public LevelDTO fromEntityToDTO(ParkingLevelEntity entity){
        LevelDTO level = new LevelDTO();
        level.setId(entity.getId());
        level.setFloor(entity.getFloor());
        level.setNumberOfSpaces((entity.getNumberOfSpaces()));
        return level;
    }

    public List<LevelDTO> fromEntityListToDTOList(List<ParkingLevelEntity> entities){
        List<LevelDTO> levels = new ArrayList<>();
        for (ParkingLevelEntity level : entities){
            levels.add(
                    fromEntityToDTO(level)
            );
        }

        return levels;
    }
}
