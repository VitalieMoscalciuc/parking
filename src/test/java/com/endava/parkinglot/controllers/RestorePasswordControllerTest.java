package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoRequest;
import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.services.UserRegistrationService;
import com.endava.parkinglot.util.PasswordGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestorePasswordControllerTest {

    @Mock
    private UserRegistrationService userRegistrationService;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private RestorePasswordController restorePasswordController;

    @Test
    void restore_ShouldGenerateNewPasswordAndReturnResponse() {
        UserPasswordRestoreDtoRequest requestDto = new UserPasswordRestoreDtoRequest();
        requestDto.setEmail("john23@gmail.com");

        UserPasswordRestoreDtoResponse expectedResponse = new UserPasswordRestoreDtoResponse();
        expectedResponse.setMessage("Your password was updated successfully, new password was sent to your email");

        when(userRegistrationService.changeUserPasswordAndSendEmail("john23@gmail.com")).thenReturn(expectedResponse);

        ResponseEntity<UserPasswordRestoreDtoResponse> responseEntity = restorePasswordController.restore(requestDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(userRegistrationService).changeUserPasswordAndSendEmail(requestDto.getEmail());
    }

    @Test
    void restore_ShouldThrowUserNotFoundException() {
        UserPasswordRestoreDtoRequest requestDto = new UserPasswordRestoreDtoRequest();
        requestDto.setEmail("nonexistent@gmail.com");
        when(userRegistrationService.changeUserPasswordAndSendEmail(any())).thenThrow(new UserNotFoundException("User with email " + requestDto.getEmail() + " not found in system."));
        Throwable exception = assertThrows(UserNotFoundException.class, () -> restorePasswordController.restore(requestDto));
        assertEquals("User with email nonexistent@gmail.com not found in system.", exception.getMessage());
    }

    @Test
    void restore_ShouldShowInvalidEmailMessage() {
        UserPasswordRestoreDtoRequest requestDto = new UserPasswordRestoreDtoRequest();
        requestDto.setEmail("invalidgmailcom");

        UserPasswordRestoreDtoResponse expectedResponse = new UserPasswordRestoreDtoResponse();
        expectedResponse.setMessage("Invalid email. It should be like: 'example@email.com'");

        when(userRegistrationService.changeUserPasswordAndSendEmail(any())).thenReturn(expectedResponse);

        ResponseEntity<UserPasswordRestoreDtoResponse> responseEntity = restorePasswordController.restore(requestDto);

        assertEquals(expectedResponse, responseEntity.getBody());
        verifyNoInteractions(passwordGenerator);
    }
}
