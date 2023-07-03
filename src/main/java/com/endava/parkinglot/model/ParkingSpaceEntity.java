package com.endava.parkinglot.model;

import com.endava.parkinglot.enums.SpaceState;
import com.endava.parkinglot.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_space")
public class ParkingSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("REGULAR")
    private SpaceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("AVAILABLE")
    private SpaceState state;

    @ManyToOne
    @JoinColumn(name = "parking_level_id")
    private ParkingLevelEntity parkingLevel;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
