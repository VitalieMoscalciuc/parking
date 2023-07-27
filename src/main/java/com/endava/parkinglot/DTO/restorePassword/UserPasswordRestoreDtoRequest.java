package com.endava.parkinglot.DTO.restorePassword;

import com.endava.parkinglot.DTO.ValidationGroups.NotEmptyValidationGroup;
import com.endava.parkinglot.DTO.ValidationGroups.PatternValidationGroup;
import com.endava.parkinglot.DTO.ValidationGroups.SizeValidationGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
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
@GroupSequence({NotEmptyValidationGroup.class, SizeValidationGroup.class, PatternValidationGroup.class, UserPasswordRestoreDtoRequest.class})
public class UserPasswordRestoreDtoRequest {

    @NotBlank(message = "Email cannot be empty.", groups = NotEmptyValidationGroup.class)
    @Size(min = 5, max = 320, message = "Size of the email should be from 5 to 320 characters.", groups = SizeValidationGroup.class)
    @Pattern(regexp = "^(?=.{5,320}$)(?=[a-zA-Z0-9])[a-zA-Z0-9._!#$%&'*+/=?^`{|}~-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{1,63}$", message="Invalid email. It should be like: 'example@email.com'", groups = PatternValidationGroup.class)
    private String email;

}
