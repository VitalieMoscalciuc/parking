package com.vmoscalciuc.parkinglot.DTO.auth;

import com.vmoscalciuc.parkinglot.DTO.ValidationGroups.NotEmptyValidationGroup;
import com.vmoscalciuc.parkinglot.DTO.ValidationGroups.PatternValidationGroup;
import com.vmoscalciuc.parkinglot.DTO.ValidationGroups.SizeValidationGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@GroupSequence({NotEmptyValidationGroup.class, SizeValidationGroup.class, PatternValidationGroup.class, AuthenticationDTO.class})
public class AuthenticationDTO {

    @NotBlank(message = "Email cannot be empty.", groups = NotEmptyValidationGroup.class)
    @Size(min = 5, max = 320, message = "Size of the email should be from 5 to 320 characters.", groups = SizeValidationGroup.class)
    @Pattern(regexp = "^(?=.{5,320}$)(?=[a-zA-Z0-9])[a-zA-Z0-9._!#$%&'*+/=?^`{|}~-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{1,63}$", message="Invalid email. It should be like: 'example@email.com'.", groups = PatternValidationGroup.class)
    private String email;

    @NotBlank(message = "Invalid password. Must be 5-10 characters, including symbols, upper- and lower-case letters." +
            "Should contain at least one digit, one upper-case character and one symbol")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\W_])(.{5,10})",
            message = "Invalid password. Must be 5-10 characters, including symbols, upper- and lower-case letters." +
                    "Should contain at least one digit, one upper-case character and one symbol")
    private String password;

}
