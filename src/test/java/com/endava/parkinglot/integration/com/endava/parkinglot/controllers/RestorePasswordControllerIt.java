package com.endava.parkinglot.integration.com.endava.parkinglot.controllers;

import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.integration.com.endava.parkinglot.util.Prerequisites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestorePasswordControllerIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    Prerequisites prerequisites;

    @BeforeEach
    void setUp() {
        prerequisites.clear();
        prerequisites.createUser();
    }

    @Test
    void restore_ShouldGenerateNewPasswordAndReturnResponseAndOkStatus() throws Exception {
        this.mockMvc
                .perform(post("/api/restore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "john23@gmail.com"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your password was updated successfully," +
                        " new password was sent to your email"));
    }

    @Test
    void restore_ShouldReturnErrorResponseAndStatusBadRequest() throws Exception {
        this.mockMvc
                .perform(post("/api/restore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "unexistent_user@gmail.com"
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User with email " + "unexistent_user@gmail.com" + " not found in system.",
                        result.getResolvedException().getMessage()));
    }
}
