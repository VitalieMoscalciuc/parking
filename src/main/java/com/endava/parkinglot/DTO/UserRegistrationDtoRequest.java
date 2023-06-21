package com.endava.parkinglot.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserRegistrationDtoRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @NotEmpty(message = "Name cannot be empty")
    @Pattern(regexp = "[a-zA-Z]+", message = "Only alphabetical characters are allowed, eg. 'John'")
    @Size(max = 30, message = "Maximum length is 30 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. It should be like: 'example@email.com'")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-'\";.,])[A-Za-z\\d@$!%*?&#^()_+\\-'\";.,]{5,10}$",
            message = "Invalid password.Must be 5-10 characters, including symbols, upper- and lower-case letters." +
                      "Should contain at least one digit,one upper case and one symbol")
    private String password;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^0[67][0-9]{7}$", message="Phone number must contain exactly 9 numeric characters(without +373).Starting with 0")
    private String phone;
}
