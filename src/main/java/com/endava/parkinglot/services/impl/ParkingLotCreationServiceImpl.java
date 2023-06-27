package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.ParkingLotCreationDtoRequest;
import com.endava.parkinglot.DTO.ParkingLotCreationDtoResponse;
import com.endava.parkinglot.mapper.ParkingMapper;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import com.endava.parkinglot.services.ParkingLotCreationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotCreationServiceImpl implements ParkingLotCreationService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingMapper parkingMapper;

    public ParkingLotCreationServiceImpl(ParkingLotRepository parkingLotRepository, ParkingMapper parkingMapper) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingMapper = parkingMapper;
    }

    @Override
    public ParkingLotCreationDtoResponse createParkingLot(ParkingLotCreationDtoRequest parkingLotCreationDtoRequest) {
        return null;
    }

    @Override
    public List<ParkingLotCreationDtoResponse> getAllParkingLot() {
        return null;
    }
}
