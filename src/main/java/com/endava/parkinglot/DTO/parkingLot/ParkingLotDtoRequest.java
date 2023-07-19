package com.endava.parkinglot.DTO.parkingLot;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotDtoRequest {
    @NotBlank(message = "Parking name should have between 1-70 characters")
    @Pattern(regexp = "^(?!.*[а-яА-Я]).{1,70}$", message = "Parking name does not accept Russian letters")
    @Pattern(regexp = "^(.{1,70})$", message = "Parking name should have between 1-70 characters")
    private String name;

    @NotBlank(message = "Parking address should have between 1-70 characters")
    @Pattern(regexp = "^(?!.*[а-яА-Я]).{1,70}$", message = "Parking address does not accept Russian letters")
    @Pattern(regexp = "^(.{1,70})$", message = "Parking address should have between 1-70 characters")
    private String address;

    @NotBlank(message = "Time should be provided in the format 'HH:mm-HH:mm'")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]-(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Time should be provided in the format 'HH:mm-HH:mm'")
    private String workingHours;

    @NotNull(message = "At least one day of the week must be selected")
    private Set<String> workingDays;

    private boolean closed;

    private boolean operatesNonStop;

    @NotNull(message = "Parking lot must have at least one level")
    @Size(min = 1, max = 5, message = "Parking can have at least 1 level and up to 5 levels")
    private List<@Valid LevelDtoForLot> levels;
}
