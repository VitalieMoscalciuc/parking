package com.endava.parkinglot.mapper;

import com.endava.parkinglot.DTO.ParkingLotCreationDtoRequest;
import com.endava.parkinglot.DTO.ParkingLotCreationDtoResponse;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.util.ModelMapperOptional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ParkingMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ModelMapperOptional modelMapperOptional;

    public ParkingLotEntity mapRequestDtoToEntity(ParkingLotCreationDtoRequest parkingLotCreationDtoRequest) {
        return modelMapper.map(parkingLotCreationDtoRequest, ParkingLotEntity.class);
    }

    public ParkingLotCreationDtoResponse mapEntityToResponseDto(ParkingLotEntity parkingLot) {
        return modelMapper.map(parkingLot, ParkingLotCreationDtoResponse.class);
    }

    public List<ParkingLotCreationDtoResponse> mapListEntityToListResponseDto(List<ParkingLotEntity> parkingLot) {
        return modelMapperOptional.mapList(parkingLot, ParkingLotCreationDtoResponse.class);
    }

    public List<ParkingLotEntity> mapRequestListDtoToListEntity(List<ParkingLotCreationDtoResponse> parkingLot) {
        return modelMapperOptional.mapList(parkingLot, ParkingLotEntity.class);
    }
}
