package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.UserRegistrationDtoResponse;
import com.endava.parkinglot.mapper.UserMapper;
import com.endava.parkinglot.model.Role;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Test
    void register_ShouldSaveNewUserAndReturnResponseDto() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .role(Role.REGULAR)
                .build();

        UserRegistrationDtoRequest requestDto = UserRegistrationDtoRequest.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .build();

        when(userMapper.mapRequestDtoToEntity(requestDto)).thenReturn(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    }

    @Test
    void grantAdminPermissionsById_UserWithRegularRole_ShouldThrowNotGrantedToDoActionException() {

    }

    @Test
    void grantAdminPermissionsByEmail_UserWithAdminRole_ShouldChangeUserRoleFind() {

    }

    @Test
    void grantAdminPermissionsByEmail_UserWithRegularRole_ShouldThrowNotGrantedToDoActionException() {

    }
}