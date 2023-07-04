package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.ParkingSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, Long> {

    @Query("SELECT space FROM ParkingSpaceEntity space WHERE space.parkingLevel.parkingLot.id=:lotId " +
            "AND space.parkingLevel.id=:levelId AND (LOWER(space.number) LIKE CONCAT('%', LOWER(:searchString), '%') OR :searchString IS NULL)")
    List<ParkingSpaceEntity> getAllByParkingLevelId(Long lotId, Long levelId, String searchString);

}