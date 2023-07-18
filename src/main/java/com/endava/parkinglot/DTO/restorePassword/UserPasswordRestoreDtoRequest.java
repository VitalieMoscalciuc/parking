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
    @NotBlank(message = "Email cannot be empty.")
    @Pattern(regexp = "^(?=.{5,320}$)(?=[a-zA-Z])[a-zA-Z0-9._!#$%&'*+/=?^`{|}~-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{1,63}$", message="Invalid email. It should be like: 'example@email.com'")
    private String email;
}
