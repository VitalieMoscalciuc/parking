package com.endava.parkinglot.model;

import com.endava.parkinglot.enums.LotState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_lot")
public class ParkingLotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String working_hours;

    @Column(nullable = false)
    private String working_days;

    @Column(nullable = false)
    private int number_of_levels;

    @Column(nullable = false)
    private int spaces_per_level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LotState state;
}
