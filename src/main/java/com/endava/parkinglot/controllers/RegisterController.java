package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.exceptions.ValidationCustomException;
import com.endava.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo.ValidationErrorObject;
import com.endava.parkinglot.security.JWTUtil;
import com.endava.parkinglot.services.UserRegistrationService;
import com.endava.parkinglot.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private final UserRegistrationService userRegistrationService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;

    @Autowired
    public RegisterController(UserRegistrationService userRegistrationService, UserValidator userValidator, JWTUtil jwtUtil) {
        this.userRegistrationService = userRegistrationService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegistrationDtoRequest registrationDtoRequest,
                                                                BindingResult bindingResult){
        userValidator.validate(registrationDtoRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            List<ValidationErrorObject> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> new ValidationErrorObject(error.getField(), error.getDefaultMessage()))
                    .toList();

            throw new ValidationCustomException(errors);
        }

        userRegistrationService.register(registrationDtoRequest);

        String token = jwtUtil.generateAccessToken(registrationDtoRequest.getEmail());

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PutMapping(value = "/grantAdmin", params = "userId")
    public ResponseEntity grantAdminPermissions(@RequestParam("userId") Long id) {
        userRegistrationService.grantAdminPermissions(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
