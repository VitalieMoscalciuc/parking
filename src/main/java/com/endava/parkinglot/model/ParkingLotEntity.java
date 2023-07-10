package com.endava.parkinglot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, name = "begin_working_hour")
    private LocalTime beginWorkingHour;

    @Column(nullable = false, name = "end_working_hour")
    private LocalTime endWorkingHour;

    @Column(name = "working_days", nullable = false)
    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL , orphanRemoval = true )
    private List<WorkingDaysEntity> workingDays;

    @Column(nullable = false)
    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingLevelEntity> levels;

    @ManyToMany(mappedBy = "parkingLots")
    Set<UserEntity> users;
    @Column(name = "status_close", nullable = false)
    private Boolean isClosed;
}