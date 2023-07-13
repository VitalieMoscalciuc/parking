package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.WorkingDaysEntity;
import jakarta.persistence.SequenceGenerators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WorkingDaysRepository extends JpaRepository<WorkingDaysEntity, Integer> {

    @Modifying
    @Query(value = "DELETE FROM working_days WHERE working_days.lot_id=:id", nativeQuery = true)
    void deleteAllByParkingLotId(Long id);
}
