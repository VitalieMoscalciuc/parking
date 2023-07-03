package com.endava.parkinglot.DTO.restorePassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserPasswordRestoreDtoRequest {
    @NotBlank(message = "Invalid email. It should be like: 'example@email.com'")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message="Invalid email. It should be like: 'example@email.com'")
    private String email;
}
