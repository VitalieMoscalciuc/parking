package com.endava.parkinglot.model.repository;

import com.endava.parkinglot.model.QRCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCodeEntity,Long> {
}