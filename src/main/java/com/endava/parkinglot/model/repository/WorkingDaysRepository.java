package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.WorkingDaysEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkingDaysRepository extends JpaRepository<WorkingDaysEntity, Integer> {

    @Modifying
    @Query(value = "DELETE FROM working_days WHERE working_days.lot_id=:id", nativeQuery = true)
    void deleteAllByParkingLotId(Long id);

    @Query(value = "SELECT w FROM WorkingDaysEntity w" +
            "      WHERE w.parkingLot.id = :id")
    List<WorkingDaysEntity> getWorkingsDaysByLotName(Long id);
}