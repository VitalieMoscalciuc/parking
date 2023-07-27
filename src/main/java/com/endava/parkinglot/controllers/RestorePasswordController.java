package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoRequest;
import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;
import com.endava.parkinglot.services.UserRegistrationService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restore")
public class RestorePasswordController {

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public RestorePasswordController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity<UserPasswordRestoreDtoResponse> restore(
            @Valid @RequestBody UserPasswordRestoreDtoRequest userPasswordRestoreDtoRequest,
            ServletRequest request) {
        userPasswordRestoreDtoRequest.setEmail(userPasswordRestoreDtoRequest.getEmail().toLowerCase());
        UserPasswordRestoreDtoResponse response =
                userRegistrationService.changeUserPasswordAndSendEmail(userPasswordRestoreDtoRequest.getEmail()
                        ,request.getRemoteAddr());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}