package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.ParkingLotEntity;
import jakarta.transaction.Transactional;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {

    @Query("SELECT lot FROM ParkingLotEntity lot WHERE LOWER(lot.name) LIKE CONCAT('%', LOWER(:searchString), '%') OR :searchString IS NULL")
    List<ParkingLotEntity> search(String searchString);

    @Modifying
    @Query(value = "delete from user_parking_lot where user_id = :userId and lot_id = :parkingLotId "
            ,nativeQuery = true)
    void removeUserFromParkingLot(@Param("userId") Long userId, @Param("parkingLotId") Long parkingLotId);

    @Query(value = "SELECT EXISTS(SELECT FROM user_parking_lot WHERE user_id = :userId AND lot_id = :parkingLotId)"
            ,nativeQuery = true)
    boolean checkIfUserExistsOnParkingLot(@Param("userId") Long userId, @Param("parkingLotId") Long parkingLotI);

}
