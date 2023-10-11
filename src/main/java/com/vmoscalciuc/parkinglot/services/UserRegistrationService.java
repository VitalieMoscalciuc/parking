package com.vmoscalciuc.parkinglot.services;

import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.vmoscalciuc.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;

public interface UserRegistrationService {

    UserPasswordRestoreDtoResponse changeUserPasswordAndSendEmail(String email, String userAddr);
    UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest);

    void grantAdminPermissions(Long id, String email);
}
