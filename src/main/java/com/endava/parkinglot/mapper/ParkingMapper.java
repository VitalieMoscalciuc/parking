package com.endava.parkinglot.mapper;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;
import com.endava.parkinglot.enums.LotState;
import com.endava.parkinglot.enums.WorkingDays;
import com.endava.parkinglot.model.ParkingLevelEntity;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.WorkingDaysEntity;
import com.endava.parkinglot.util.ModelMapperOptional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingMapper {

    private final ModelMapper modelMapper;

    private final ModelMapperOptional modelMapperOptional;

    public ParkingLotEntity mapRequestDtoToEntity(ParkingLotDtoRequest parkingLotDtoRequest) {
        return modelMapper.map(parkingLotDtoRequest, ParkingLotEntity.class);
    }

    public ParkingLotDtoResponse mapEntityToResponseDto(ParkingLotEntity entity) {
        ParkingLotDtoResponse response = new ParkingLotDtoResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setAddress(entity.getAddress());

        List<WorkingDays> days = new ArrayList<>();
        for (WorkingDaysEntity workingDaysEntity : entity.getWorking_days()){
            days.add(workingDaysEntity.getNameOfDay());
        }
        response.setWorkingDays(days);

        List<LevelDTO> levels = new ArrayList<>();
        for (ParkingLevelEntity levelEntity : entity.getLevels()){
            LevelDTO levelDtoForLot = new LevelDTO();
            levelDtoForLot.setId(entity.getId());
            levelDtoForLot.setFloor(levelEntity.getFloor());
            levelDtoForLot.setNumberOfSpaces(levelEntity.getNumberOfSpaces());
            levels.add(levelDtoForLot);
        }
        response.setLevels(levels);

        response.setWorkingHours(entity.getBeginWorkingHour().toString() + " - " + entity.getEndWorkingHour().toString());

        response.setClosed(entity.getState() != LotState.OPEN);

        response.setOperatesNonStop(entity.getBeginWorkingHour().toString().contains("00:") && entity.getEndWorkingHour().toString().contains("24:"));

        return response;
    }

    public List<ParkingLotDtoResponse> mapListEntityToListResponseDto(List<ParkingLotEntity> parkingLot) {
        List<ParkingLotDtoResponse> responseList = new ArrayList<>();
        for (ParkingLotEntity entity : parkingLot){
            responseList.add(
                    mapEntityToResponseDto(entity)
            );
        }

        return responseList;
    }

    public List<ParkingLotEntity> mapRequestListDtoToListEntity(List<ParkingLotDtoResponse> parkingLot) {
        return modelMapperOptional.mapList(parkingLot, ParkingLotEntity.class);
    }
}
