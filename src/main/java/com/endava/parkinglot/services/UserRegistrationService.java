package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.UserRegistrationDtoResponse;

public interface UserRegistrationService {


    UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest);

    void grantAdminPermissions(Long id, String email);
}
