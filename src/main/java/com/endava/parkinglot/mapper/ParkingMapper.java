package com.endava.parkinglot.mapper;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;
import com.endava.parkinglot.DTO.parkingLot.LevelDtoForLot;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoRequest;
import com.endava.parkinglot.DTO.parkingLot.ParkingLotDtoResponse;

import com.endava.parkinglot.enums.WorkingDays;
import com.endava.parkinglot.model.ParkingLevelEntity;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.WorkingDaysEntity;
import com.endava.parkinglot.util.ModelMapperOptional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ParkingMapper {

    private final ModelMapper modelMapper;

    private final ModelMapperOptional modelMapperOptional;

    public ParkingLotEntity mapRequestDtoToEntity(ParkingLotDtoRequest parkingLotDtoRequest) {
        if (parkingLotDtoRequest.getOperatesNonStop()) {
            parkingLotDtoRequest.setWorkingDays(Set.of("SUNDAY", "MONDAY", "TUESDAY",
                    "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"));
            parkingLotDtoRequest.setWorkingHours("00:00-23:59");
        }

        var lot = ParkingLotEntity.builder()
                .name(parkingLotDtoRequest.getName())
                .address(parkingLotDtoRequest.getAddress())
                .beginWorkingHour(stringToTime(parkingLotDtoRequest.getWorkingHours(), 0))
                .endWorkingHour(stringToTime(parkingLotDtoRequest.getWorkingHours(), 1))
                .workingDays(stringToWorkinDayList(parkingLotDtoRequest.getWorkingDays()))
                .levels(toLevelEntityList(parkingLotDtoRequest.getLevels()))
                .isClosed(parkingLotDtoRequest.getIsClosed())
                .build();

        lot.getWorkingDays().forEach(day -> day.setParkingLot(lot));
        lot.getLevels().forEach(level -> level.setParkingLot(lot));

        return lot;
    }

    private LocalTime stringToTime(String hours, int i) {
        String[] beginHours = hours.split("-");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(beginHours[i], formatter);
    }

    private List<WorkingDaysEntity> stringToWorkinDayList(Set<String> days) {
        List<WorkingDaysEntity> workingDays = new ArrayList<>();

        for(String day: days) {
            WorkingDaysEntity weekDay = new WorkingDaysEntity();
           switch (day) {
               case "SUNDAY" -> weekDay.setNameOfDay(WorkingDays.SUNDAY);
               case "MONDAY" -> weekDay.setNameOfDay(WorkingDays.MONDAY);
               case "TUESDAY"-> weekDay.setNameOfDay(WorkingDays.TUESDAY);
               case "WEDNESDAY" -> weekDay.setNameOfDay(WorkingDays.WEDNESDAY);
               case "THURSDAY" -> weekDay.setNameOfDay(WorkingDays.THURSDAY);
               case "FRIDAY" -> weekDay.setNameOfDay(WorkingDays.FRIDAY);
               case "SATURDAY" -> weekDay.setNameOfDay(WorkingDays.SATURDAY);
           }
           workingDays.add(weekDay);
        }
        return workingDays;
    }

    private List<ParkingLevelEntity> toLevelEntityList(List<LevelDtoForLot> levels) {
        List<ParkingLevelEntity> levelsEntity = new ArrayList<>();

        for (LevelDtoForLot level: levels) {
            ParkingLevelEntity levelEntity = new ParkingLevelEntity();
            levelEntity.setFloor(level.getFloor());
            levelEntity.setNumberOfSpaces(Integer.parseInt(level.getNumberOfSpaces()));
            levelsEntity.add(levelEntity);
        }
        return levelsEntity;
    }

    public ParkingLotDtoResponse mapEntityToResponseDto(ParkingLotEntity entity) {
        ParkingLotDtoResponse response = new ParkingLotDtoResponse();
        response.setName(entity.getName());
        response.setAddress(entity.getAddress());

        List<WorkingDays> days = new ArrayList<>();
        for (WorkingDaysEntity workingDaysEntity : entity.getWorkingDays()){
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

        response.setClosed(entity.getIsClosed());

        response.setOperatesNonStop(entity.getBeginWorkingHour().toString().contains("00:00") && entity.getEndWorkingHour().toString().contains("23:59"));

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

    public List<ParkingLevelEntity> mapToLevelEntityList(List<LevelDtoForLot> levelsDto) {
        return modelMapperOptional.mapList(levelsDto, ParkingLevelEntity.class);
    }
}