package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
}
