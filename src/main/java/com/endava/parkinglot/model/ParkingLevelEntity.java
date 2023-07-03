package com.endava.parkinglot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLotEntity parkingLot;

    @OneToMany(mappedBy = "parkingLevel", cascade = CascadeType.PERSIST)
    private List<ParkingSpaceEntity> parkingSpaces;
}
