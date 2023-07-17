package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoRequest;
import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.services.UserRegistrationService;
import com.endava.parkinglot.util.PasswordGenerator;
import jakarta.servlet.ServletRequest;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestorePasswordControllerTest {

    @Mock
    private UserRegistrationService userRegistrationService;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private ServletRequest request;

    @InjectMocks
    private RestorePasswordController restorePasswordController;

    @Test
    void restore_ShouldGenerateNewPasswordAndReturnResponse() {
        UserPasswordRestoreDtoRequest requestDto = new UserPasswordRestoreDtoRequest();
        requestDto.setEmail("john23@gmail.com");
        String userIp = "192.168.1.1";

        UserPasswordRestoreDtoResponse expectedResponse = new UserPasswordRestoreDtoResponse();
        expectedResponse.setMessage("Your password was updated successfully, new password was sent to your email");


        when(request.getRemoteAddr()).thenReturn(userIp);
        when(userRegistrationService.changeUserPasswordAndSendEmail(requestDto.getEmail(), userIp))
                .thenReturn(expectedResponse);

        ResponseEntity<UserPasswordRestoreDtoResponse> responseEntity = restorePasswordController
                .restore(requestDto,request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(userRegistrationService).changeUserPasswordAndSendEmail(requestDto.getEmail(),userIp);
    }

    @Test
    void restore_ShouldShowInvalidEmailMessage() {
        UserPasswordRestoreDtoRequest requestDto = new UserPasswordRestoreDtoRequest();
        requestDto.setEmail("invalidgmailcom");

        UserPasswordRestoreDtoResponse expectedResponse = new UserPasswordRestoreDtoResponse();
        expectedResponse.setMessage("Invalid email. It should be like: 'example@email.com'");

        when(userRegistrationService.changeUserPasswordAndSendEmail(any(),any())).thenReturn(expectedResponse);

        ResponseEntity<UserPasswordRestoreDtoResponse> responseEntity = restorePasswordController.restore(requestDto,request);

        assertEquals(expectedResponse, responseEntity.getBody());
        verifyNoInteractions(passwordGenerator);
    }
}
