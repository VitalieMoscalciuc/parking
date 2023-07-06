package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {

    @Query("SELECT lot FROM ParkingLotEntity lot " +
            "JOIN lot.users user WHERE user.email=:userEmail " +
            "AND (LOWER(lot.name) LIKE CONCAT('%', LOWER(:searchString), '%') OR :searchString IS NULL)")
    List<ParkingLotEntity> search(String searchString, String userEmail);

    @Modifying
    @Query(value = "delete from user_parking_lot where user_id = :userId and lot_id = :parkingLotId "
            ,nativeQuery = true)
    void removeUserFromParkingLot(@Param("userId") Long userId, @Param("parkingLotId") Long parkingLotId);

    @Query(value = "SELECT EXISTS(SELECT FROM user_parking_lot WHERE user_id = :userId AND lot_id = :parkingLotId)"
            ,nativeQuery = true)
    boolean checkIfUserExistsOnParkingLotByUserId(@Param("userId") Long userId, @Param("parkingLotId") Long parkingLotId);

    @Query(value = "SELECT EXISTS(SELECT FROM user_table" +
                    "    INNER JOIN user_parking_lot" +
                    "        ON user_table.id = user_parking_lot.user_id" +
                    "            WHERE user_table.email = :email" +
                    "               AND user_parking_lot.lot_id = :parkingLotId)"
            ,nativeQuery = true)
    boolean checkIfUserExistsOnParkingLotByUserEmail(@Param("email") String email, @Param("parkingLotId") Long parkingLotId);


    Optional<ParkingLotEntity> findByName(String name);

    Optional<ParkingLotEntity> findByAddress(String address);
}
