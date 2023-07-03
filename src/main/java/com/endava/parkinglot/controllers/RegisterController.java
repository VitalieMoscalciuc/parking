package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.endava.parkinglot.DTO.auth.AuthAndRegistrationResponseDTO;
import com.endava.parkinglot.exceptions.validation.ValidationCustomException;
import com.endava.parkinglot.security.JWTUtil;
import com.endava.parkinglot.services.UserRegistrationService;
import com.endava.parkinglot.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

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
    public ResponseEntity<AuthAndRegistrationResponseDTO> register(@RequestBody @Valid UserRegistrationDtoRequest registrationDtoRequest,
                                                                   BindingResult bindingResult) {
        userValidator.validate(registrationDtoRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                if (!errors.containsKey(error.getField())) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            }

            throw new ValidationCustomException(errors);
        }

        UserRegistrationDtoResponse created = userRegistrationService.register(registrationDtoRequest);

        String token = jwtUtil.generateAccessToken(registrationDtoRequest.getEmail());

        AuthAndRegistrationResponseDTO response = new AuthAndRegistrationResponseDTO(
                created.getEmail(),
                created.getRole().toString(),
                token
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/grantAdmin")
    public ResponseEntity<HttpStatus> grantAdminPermissions(@RequestParam(value = "userId", required = false) Long userId,
                                                @RequestParam(value = "email", required = false) String email) {
        userRegistrationService.grantAdminPermissions(userId, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
