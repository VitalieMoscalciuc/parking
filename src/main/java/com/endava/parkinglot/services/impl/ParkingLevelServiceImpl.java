package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.parkingLevel.LevelDTO;
import com.endava.parkinglot.exceptions.parkingLot.ParkingLotNotFoundException;
import com.endava.parkinglot.mapper.ParkingLevelMapper;
import com.endava.parkinglot.model.ParkingLevelEntity;
import com.endava.parkinglot.model.repository.ParkingLevelRepository;
import com.endava.parkinglot.services.ParkingLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLevelServiceImpl implements ParkingLevelService {

    private final ParkingLevelMapper levelMapper;
    private final ParkingLevelRepository parkingLevelRepository;
    private static final Logger logger = LoggerFactory.getLogger(ParkingLevelServiceImpl.class);

    @Autowired
    public ParkingLevelServiceImpl(ParkingLevelMapper levelMapper, ParkingLevelRepository parkingLevelRepository) {
        this.levelMapper = levelMapper;
        this.parkingLevelRepository = parkingLevelRepository;
    }

    @Override
    public List<LevelDTO> getAllByLotId(Long lotId) {
        logger.info("Trying to get all levels by specific parking lot id: " + lotId);
        List<ParkingLevelEntity> entities = parkingLevelRepository.getAllByParkingLotId(lotId);
        if (entities.isEmpty()){
            logger.error("There is no parking lot with such id: " + lotId);
            throw new ParkingLotNotFoundException(lotId);
        }

        return levelMapper.fromEntityListToDTOList(entities);
    }
}
