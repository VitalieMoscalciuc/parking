package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.endava.parkinglot.DTO.auth.AuthAndRegistrationResponseDTO;
import com.endava.parkinglot.enums.Role;
import com.endava.parkinglot.exceptions.validation.ValidationCustomException;
import com.endava.parkinglot.security.JWTUtil;
import com.endava.parkinglot.services.UserRegistrationService;
import com.endava.parkinglot.util.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserRegistrationService registrationService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private JWTUtil jwtUtil;
    @InjectMocks
    private RegisterController registerController;

    @Test
    void register_WithValidData_ShouldRegisterTheUser() {
        UserRegistrationDtoRequest registrationDtoRequest = UserRegistrationDtoRequest.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .build();

        when(!bindingResult.hasErrors()).thenReturn(false);

        UserRegistrationDtoResponse registrationResponse = UserRegistrationDtoResponse.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .role(Role.REGULAR)
                .build();

        when(registrationService.register(registrationDtoRequest)).thenReturn(registrationResponse);
        String token = "TestToken1111";
        when(jwtUtil.generateAccessToken(registrationDtoRequest.getEmail())).thenReturn(token);

        AuthAndRegistrationResponseDTO expectedAuthResponse = AuthAndRegistrationResponseDTO.builder()
                .email("john23@gmail.com")
                .role(Role.REGULAR.toString())
                .jwt(token)
                .build();

        ResponseEntity<AuthAndRegistrationResponseDTO> response = registerController.register(registrationDtoRequest, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertAll(
                () -> assertEquals(expectedAuthResponse.getEmail(), Objects.requireNonNull(response.getBody()).getEmail()),
                () -> assertEquals(expectedAuthResponse.getRole(), Objects.requireNonNull(response.getBody()).getRole()),
                () -> assertEquals(expectedAuthResponse.getJwt(), Objects.requireNonNull(response.getBody()).getJwt())
        );

        verify(userValidator).validate(registrationDtoRequest, bindingResult);
        verify(registrationService).register(registrationDtoRequest);
        verify(jwtUtil).generateAccessToken(registrationDtoRequest.getEmail());
    }

    @Test
    void register_WithInvalidData_ShouldThrowValidationCustomException() {
        UserRegistrationDtoRequest invalidRequestDto = UserRegistrationDtoRequest.builder()
                .email("john23gmail.com")
                .name("")
                .password("Jon2")
                .phone("0681122334444")
                .build();

        when(bindingResult.hasErrors()).thenReturn(true);

        List<FieldError> errors = List.of(
                new FieldError("invalidRequestDto", "phone",
                        "Phone number must contain exactly 9 numeric characters(without +373).Starting with 0"),
                new FieldError("invalidRequestDto", "password",
                        "Invalid password.Must be 5-10 characters, including symbols, " +
                         "upper- and lower-case letters.Should contain at least one digit,one upper case and one symbol"),
                new FieldError(
                        "invalidRequestDto", "email",
                        "Invalid email. It should be like: 'example@email.com'")
        );

        when(bindingResult.getFieldErrors()).thenReturn(errors);

        assertThrows(ValidationCustomException.class, () -> {
            registerController.register(invalidRequestDto, bindingResult);
        });

        verify(userValidator).validate(invalidRequestDto, bindingResult);
        verifyNoInteractions(registrationService);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void grandAdminPermissionsTest_ShouldReturnResponseEntityStatusOk() {
      String email = "vasilii@gmail.com";
      Long id = 2L;

      doNothing().when(registrationService).grantAdminPermissions(id, email);

      assertEquals(HttpStatus.OK, registerController.grantAdminPermissions(id, email).getStatusCode());

      verify(registrationService).grantAdminPermissions(id, email);
    }
}