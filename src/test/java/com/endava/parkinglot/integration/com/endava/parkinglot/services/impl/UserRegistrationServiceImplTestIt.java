package com.endava.parkinglot.integration.com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;
import com.endava.parkinglot.integration.com.endava.parkinglot.util.Prerequisites;
import com.endava.parkinglot.services.UserRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class UserRegistrationServiceImplTestIt {
    @Autowired
    private Prerequisites prerequisites;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {
        prerequisites.clear();
        prerequisites.createUser();

    }

    @Test
    public void shouldGenerateNewPassword() {
        String email = "john23@gmail.com";
        String userIp = "192.168.1.1";
        UserPasswordRestoreDtoResponse expectedResponse = UserPasswordRestoreDtoResponse.builder()
                .message("If your email is valid, your password will be sent to  your email")
                .build();
        UserPasswordRestoreDtoResponse response = userRegistrationService.changeUserPasswordAndSendEmail(email,userIp);
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
