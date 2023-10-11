package com.vmoscalciuc.parkinglot.model;

import com.vmoscalciuc.parkinglot.enums.WorkingDays;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "working_days")
@Entity
public class WorkingDaysEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_of_day")
    @Enumerated(EnumType.STRING)
    private WorkingDays nameOfDay;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLotEntity parkingLot;
}