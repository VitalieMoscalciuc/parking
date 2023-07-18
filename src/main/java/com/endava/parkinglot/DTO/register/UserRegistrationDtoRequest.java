package com.endava.parkinglot.DTO.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDtoRequest {

    @NotBlank(message = "Only alphabetical characters are allowed, maximum length is 30 characters")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Only alphabetical characters are allowed, maximum length is 30 characters")
    @Size(max = 30, message = "Only alphabetical characters are allowed, maximum length is 30 characters")
    private String name;

    @NotBlank(message = "Invalid email. It must follow the standard email pattern: example@gmail.com.")
    @Pattern(regexp = "^(?=.{5,320}$)(?=[a-zA-Z])[a-zA-Z0-9._!#$%&'*+/=?^`{|}~-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{1,63}$", message="Invalid email. It should be like: 'example@email.com'")
    private String email;

    @NotBlank(message = "Invalid password.Must be 5-10 characters, including symbols, upper- and lower-case letters." +
            "Should contain at least one digit,one upper case and one symbol")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\W_])(.{5,10})",
            message = "Invalid password. Must be 5-10 characters, including symbols, upper- and lower-case letters. " +
                    "Should contain at least one digit, one uppercase letter, and one symbol")
    private String password;

    @NotBlank(message = "Phone number must contain exactly 9 numeric characters(without +373).Starting with 0")
    @Pattern(regexp = "^0[0-9]{8}$", message="Phone number must contain exactly 9 numeric characters(without +373).Starting with 0")
    private String phone;
}
