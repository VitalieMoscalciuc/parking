package com.endava.parkinglot.DTO;

import com.endava.parkinglot.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDtoResponse {
    private String name;
    private String email;
    private Role role;
}
