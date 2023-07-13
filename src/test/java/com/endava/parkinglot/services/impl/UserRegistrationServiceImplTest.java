package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.endava.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;
import com.endava.parkinglot.enums.Role;
import com.endava.parkinglot.exceptions.email.FailedEmailNotificationException;
import com.endava.parkinglot.exceptions.user.UserNotFoundException;
import com.endava.parkinglot.exceptions.validation.ValidationCustomException;
import com.endava.parkinglot.mapper.UserMapper;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import com.endava.parkinglot.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordGenerator passwordGenerator;
    @Mock
    private EmailNotificationServiceImpl emailNotificationService;
    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    private UserEntity user;

    @BeforeEach
    void setup() {
        user = UserEntity.builder()
                .id(1L)
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .role(Role.REGULAR)
                .build();
    }

    @Test
    void register_ShouldSaveNewUserAndReturnResponseDto() {


        UserRegistrationDtoRequest requestDto = UserRegistrationDtoRequest.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .build();

        when(userMapper.mapRequestDtoToEntity(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword().trim())).thenReturn(anyString());

        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        user.setName(user.getName().trim().replaceAll("\\s+", " "));
        user.setEnabled(true);
        user.setRole(Role.REGULAR);

        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapEntityToResponseDto(user)).thenReturn(any(UserRegistrationDtoResponse.class));

        userRegistrationService.register(requestDto);

        verify(userMapper).mapRequestDtoToEntity(requestDto);
        verify(userRepository).save(user);
        verify(userMapper).mapEntityToResponseDto(user);
    }

    @Test
    void grantAdminPermissionsById_UserWithAdminRole_ShouldChangeUserRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userRegistrationService.grantAdminPermissionsById(1L);

        verify(userRepository).save(any());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void grantAdminPermissionsById_UserWithRegularRole_ShouldThrowNotGrantedToDoActionException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.grantAdminPermissionsById(anyLong());
        });
        assertTrue(exception.getMessage().contains("User with ID"));
        assertTrue(exception.getMessage().contains("not found."));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void grantAdminPermissionsByEmail_UserWithAdminRole_ShouldChangeUserRoleFind() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userRegistrationService.grantAdminPermissionsByEmail("john23@gmail.com");

        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void grantAdminPermissionsByEmail_UserWithRegularRole_ShouldThrowNotGrantedToDoActionException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.grantAdminPermissionsByEmail(anyString());
        });

        assertTrue(exception.getMessage().contains("User with email"));
        assertTrue(exception.getMessage().contains("not found."));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testGrantAdminPermissions_WhenNotValidEmailProvided(){
        ValidationCustomException exception = assertThrows(ValidationCustomException.class, () -> {
            userRegistrationService.grantAdminPermissions(null, "nikbud03gmail.com");
        });

        assertTrue(exception.getErrorObjectMap().containsKey("email"));
        assertEquals("Invalid email. It should be like: 'example@email.com'" ,exception.getErrorObjectMap().get("email"));
    }

    @Test
    void testGrantAdminPermissions_WhenNotValidEmailProvided2(){
        ValidationCustomException exception = assertThrows(ValidationCustomException.class, () -> {
            userRegistrationService.grantAdminPermissions(null, "nikbud03@gmailcom");
        });

        assertTrue(exception.getErrorObjectMap().containsKey("email"));
        assertEquals("Invalid email. It should be like: 'example@email.com'" ,exception.getErrorObjectMap().get("email"));
    }

    @Test
    void testGrantAdminPermissions_WhenValidIdProvidedAndEmailIsNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> {
            userRegistrationService.grantAdminPermissions(1L, null);
        });

        verify(emailNotificationService , times(1)).sendNotificationAboutGrantedAdminRole(anyString());
        verify(emailNotificationService).sendNotificationAboutGrantedAdminRole(user.getEmail());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void testGrantAdminPermissions_WhenValidEmailProvidedAndIdIsNull() {
        when(userRepository.findByEmail("john23@gmail.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> {
            userRegistrationService.grantAdminPermissions(null, "john23@gmail.com");
        });

        verify(emailNotificationService , times(1)).sendNotificationAboutGrantedAdminRole(anyString());
        verify(emailNotificationService).sendNotificationAboutGrantedAdminRole(user.getEmail());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void testGrantAdminPermissions_WhenNullIdAndNullEmailProvided_ThenUserNotFoundExceptionIsThrown(){
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.grantAdminPermissions(null, null);
        });

        assertEquals("Missing user ID or email.", exception.getMessage());
    }

    @Test
    void generateNewPassword_WhenUserExists_ShouldUpdatePasswordAndSendEmail() throws FailedEmailNotificationException {
        String userIp = "192.168.1.1";
        String email = "john23@gmail.com";
        String newPassword = "newPassword";
        String encryptedPassword = "encryptedPassword";
        UserEntity user = UserEntity.builder()
                .email(email)
                .build();

        UserPasswordRestoreDtoResponse expectedResponse = UserPasswordRestoreDtoResponse.builder()
                .message("If your email is valid, your password will be sent to  your email")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordGenerator.generateRandomPassword()).thenReturn(newPassword);
        when(passwordEncoder.encode(newPassword)).thenReturn(encryptedPassword);

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

        UserPasswordRestoreDtoResponse response = userRegistrationService.changeUserPasswordAndSendEmail(email,userIp);

        verify(userRepository).findByEmail(email);
        verify(passwordGenerator).generateRandomPassword();
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).updateUserEntityByPassword(passwordCaptor.capture(), eq(email));
        verify(emailNotificationService).sendNewPassword(eq(email), eq(newPassword));

        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(encryptedPassword, passwordCaptor.getValue());
    }


    @Test
    void generateNewPassword_WhenUserDoesNotExist_ShouldShowMessage() {
        UserPasswordRestoreDtoResponse expectedResponse = UserPasswordRestoreDtoResponse.builder()
                .message("If your email is valid, your password will be sent to  your email")
                .build();

        String email = "nonexistent@gmail.com";
        String userIp = "192.168.1.1";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserPasswordRestoreDtoResponse response = userRegistrationService.changeUserPasswordAndSendEmail(email,userIp);

        assertEquals(expectedResponse.getMessage(), response.getMessage());

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository, passwordGenerator, passwordEncoder, emailNotificationService);
    }

    @Test
    void generateNewPassword_WhenEmailNotificationFails_ShouldSetErrorMessageAndLogWarning() throws FailedEmailNotificationException {
        String email = "john23@gmail.com";
        String userIp = "192.168.1.1";
        String newPassword = "newPassword";
        String encryptedPassword = "encryptedPassword";
        UserEntity user = UserEntity.builder()
                .email(email)
                .build();
        UserPasswordRestoreDtoResponse expectedResponse = UserPasswordRestoreDtoResponse.builder()
                .message("Couldn't send email with new password, please try again !")
                .build();
        FailedEmailNotificationException emailException = new FailedEmailNotificationException("Email sending failed");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordGenerator.generateRandomPassword()).thenReturn(newPassword);
        when(passwordEncoder.encode(newPassword)).thenReturn(encryptedPassword);
        doThrow(emailException).when(emailNotificationService).sendNewPassword(eq(email), eq(newPassword));

        assertThrows(FailedEmailNotificationException.class, () -> {
            userRegistrationService.changeUserPasswordAndSendEmail(email,userIp);
        });

        verify(userRepository).findByEmail(email);
        verify(passwordGenerator).generateRandomPassword();
        verify(passwordEncoder).encode(newPassword);
        verify(emailNotificationService).sendNewPassword(eq(email), eq(newPassword));
    }
}