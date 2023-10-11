package com.vmoscalciuc.parkinglot.mapper;

import com.vmoscalciuc.parkinglot.DTO.parkingSpace.SpaceDTO;
import com.vmoscalciuc.parkinglot.enums.SpaceState;
import com.vmoscalciuc.parkinglot.enums.SpaceType;
import com.vmoscalciuc.parkinglot.model.ParkingSpaceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParkingSpaceMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ParkingSpaceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SpaceDTO mapEntityToDTO(ParkingSpaceEntity entity){
        SpaceDTO space = modelMapper.map(entity, SpaceDTO.class);
        if (space.getType() != SpaceType.TEMPORARILY_CLOSED){
            if (entity.getUser() == null) space.setState(SpaceState.AVAILABLE);
            else space.setState(SpaceState.OCCUPIED);
        }

        return space;
    }

    public List<SpaceDTO> mapEntityListToDTOlist(List<ParkingSpaceEntity> entities){
        List<SpaceDTO> spaces = new ArrayList<>();
        for (ParkingSpaceEntity space : entities){
            spaces.add(mapEntityToDTO(space));
        }
        return spaces;
    }
}
