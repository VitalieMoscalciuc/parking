package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.register.UserRegistrationDtoResponse;

public interface UserRegistrationService {


    UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest);

    void grantAdminPermissions(Long id, String email);
}
