package com.vmoscalciuc.parkinglot.integration.com.vmoscalciuc.parkinglot.controllers.controllers;

import com.vmoscalciuc.parkinglot.integration.com.vmoscalciuc.parkinglot.controllers.util.Prerequisites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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
                .andExpect(jsonPath("$.message").value("If your email is valid, your password " +
                        "will be sent to  your email"));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("If your email is valid, your password " +
                        "will be sent to  your email"));
    }
}
