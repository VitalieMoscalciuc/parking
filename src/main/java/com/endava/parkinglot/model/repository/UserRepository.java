package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.password = ?1 WHERE u.email=?2")
    void updateUserEntityByPassword(String password,String email);
}
