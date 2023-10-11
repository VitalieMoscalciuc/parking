package com.vmoscalciuc.parkinglot.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PasswordGeneratorTest {

    @InjectMocks
    private PasswordGenerator passwordGenerator;

    @Test
    void register_ShouldGenerateNewPassword() {
        String newPassword = passwordGenerator.generateRandomPassword();
        String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-'\";.,])[A-Za-z\\d@$!%*?&#^()_+\\-'\";.,]{5,10}$";

        assertNotNull("Password shouldn't be null", newPassword);
        assertTrue("Password should match the regex pattern", newPassword.matches(regexPattern));
    }
}
