package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.ParkingLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLevelRepository extends JpaRepository<ParkingLevelEntity, Long> {

    @Query("SELECT level FROM ParkingLevelEntity level WHERE level.parkingLot.id = :id")
    List<ParkingLevelEntity> getAllByParkingLotId(Long id);
}
