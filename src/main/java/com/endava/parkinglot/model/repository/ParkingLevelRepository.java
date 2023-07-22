package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.ParkingLevelEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLevelRepository extends JpaRepository<ParkingLevelEntity, Long> {

    @Query("SELECT level FROM ParkingLevelEntity level WHERE level.parkingLot.id = :id")
    List<ParkingLevelEntity> getAllByParkingLotId(Long id);

    @Modifying
    @Query(value = "DELETE FROM parking_level WHERE id=:id", nativeQuery = true)
    void deleteById(@NotNull Long id);

    @Modifying
    @Query("DELETE FROM ParkingLevelEntity level WHERE level.parkingLot.id=:id")
    void deleteAllByParkingLotId(Long id);
}
