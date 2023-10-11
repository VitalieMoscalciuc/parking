package com.vmoscalciuc.parkinglot.controllers;

import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.vmoscalciuc.parkinglot.DTO.auth.AuthAndRegistrationResponseDTO;
import com.vmoscalciuc.parkinglot.exceptions.validation.ValidationCustomException;
import com.vmoscalciuc.parkinglot.security.JWTUtil;
import com.vmoscalciuc.parkinglot.services.UserRegistrationService;
import com.vmoscalciuc.parkinglot.validators.UserValidator;
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
        registrationDtoRequest.setEmail(registrationDtoRequest.getEmail().toLowerCase());

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

        String token = jwtUtil.generateAccessToken(registrationDtoRequest.getEmail(), "REGULAR");

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
