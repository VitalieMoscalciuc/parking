package com.endava.parkinglot.model;

import com.endava.parkinglot.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phone;

    @Column(name = "enabled")
    @ColumnDefault("true")
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_parking_lot",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lot_id")
    )
    Set<ParkingLotEntity> parkingLots;
}