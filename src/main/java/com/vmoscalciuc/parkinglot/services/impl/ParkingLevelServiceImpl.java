package com.vmoscalciuc.parkinglot.services.impl;

import com.vmoscalciuc.parkinglot.DTO.parkingLevel.LevelDTO;
import com.vmoscalciuc.parkinglot.exceptions.parkingLot.ParkingLotNotFoundException;
import com.vmoscalciuc.parkinglot.mapper.ParkingLevelMapper;
import com.vmoscalciuc.parkinglot.model.ParkingLevelEntity;
import com.vmoscalciuc.parkinglot.model.repository.ParkingLevelRepository;
import com.vmoscalciuc.parkinglot.services.ParkingLevelService;
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
