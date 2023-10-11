package com.vmoscalciuc.parkinglot.model.repository;

import com.vmoscalciuc.parkinglot.model.QRCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCodeEntity,Long> {
}