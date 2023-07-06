package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.security.JWTUtil;
import com.endava.parkinglot.util.UserCreationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmailNotificationServiceImplTest {

    @Autowired
    private UserCreationUtils userCreationUtils;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JWTUtil jwtUtil;

    private UserEntity adminUser;

    private UserEntity regularUser;

    @BeforeEach
    void setUp() {
        userCreationUtils.clearAll();
        adminUser = userCreationUtils.createAdminUser();
        regularUser = userCreationUtils.createRegularUser();
    }

    @Test
    void shouldSendEmailNotificationAboutGrantedAdminAuthority() {
        // Arrange
        String email = regularUser.getEmail();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtUtil.generateAccessToken(adminUser.getEmail()));
        HttpEntity<String> emailRequest = new HttpEntity<>(email, headers);

        // Act
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/register/grantAdmin?userId=2",
                HttpMethod.PUT,
                emailRequest,
                Void.class
        );

        // Assertions
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
