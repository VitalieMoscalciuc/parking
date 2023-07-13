package com.endava.parkinglot.services;

import com.endava.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;

public interface UserRegistrationService {

    UserPasswordRestoreDtoResponse changeUserPasswordAndSendEmail(String email,String userAddr);
    UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest);

    void grantAdminPermissions(Long id, String email);
}
