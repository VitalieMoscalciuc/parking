package com.endava.parkinglot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_level")
public class ParkingLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Character floor;

    @Column(name = "number_of_spaces", nullable = false)
    private Integer numberOfSpaces;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lot_id")
    private ParkingLotEntity parkingLot;

    @OneToMany(mappedBy = "parkingLevel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSpaceEntity> parkingSpaces;

    public void addSpacesToLevel(List<ParkingSpaceEntity> spaces) {
        parkingSpaces = new ArrayList<>();
        for(ParkingSpaceEntity space: spaces) {
            parkingSpaces.add(space);
            space.setParkingLevel(this);
        }
    }
}