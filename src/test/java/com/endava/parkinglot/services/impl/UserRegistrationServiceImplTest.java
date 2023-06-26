package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.UserRegistrationDtoResponse;
import com.endava.parkinglot.exceptions.UserNotGrantedToDoActionException;
import com.endava.parkinglot.mapper.UserMapper;
import com.endava.parkinglot.model.Role;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static java.util.Collections.singletonList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
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
        final Long userId = 1L;
        final UserEntity user = UserEntity.builder()
                .id(userId)
                .build();

        when(authentication.getPrincipal()).thenReturn(buildAdminUser("ROLE_ADMIN"));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userRegistrationService.grantAdminPermissionsById(userId);

        verify(userRepository).findById(anyLong());
        verify(userRepository).save(user);
    }

    @Test
    void grantAdminPermissionsById_UserWithRegularRole_ShouldThrowNotGrantedToDoActionException() {
        final Long userId = 1L;
        final UserEntity user = UserEntity.builder()
                .id(userId)
                .build();

        when(authentication.getPrincipal()).thenReturn(buildAdminUser("ROLE_REGULAR"));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotGrantedToDoActionException.class,
                () -> userRegistrationService.grantAdminPermissionsById(userId), "User doesn't have authorities to do this action");
    }

    @Test
    void grantAdminPermissionsByEmail_UserWithAdminRole_ShouldChangeUserRoleFind() {
        final String userEmail = "vasilii@gmail.com";
        final UserEntity user = UserEntity.builder()
                .email(userEmail)
                .build();

        when(authentication.getPrincipal()).thenReturn(buildAdminUser("ROLE_ADMIN"));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        userRegistrationService.grantAdminPermissionsByEmail(userEmail);

        verify(userRepository).findByEmail(userEmail);
        verify(userRepository).save(user);
    }

    @Test
    void grantAdminPermissionsByEmail_UserWithRegularRole_ShouldThrowNotGrantedToDoActionException() {
        final String userEmail = "vasilii@gmail.com";
        final UserEntity user = UserEntity.builder()
                .email(userEmail)
                .build();

        when(authentication.getPrincipal()).thenReturn(buildAdminUser("ROLE_REGULAR"));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotGrantedToDoActionException.class,
                () -> userRegistrationService.grantAdminPermissionsByEmail(userEmail), "User doesn't have authorities to do this action");
    }

    private UserDetails buildAdminUser(String role) {
        return User.builder()
                .username("Jonathan")
                .password("Jonathan_2")
                .authorities(singletonList(new SimpleGrantedAuthority(role)))
                .build();
    }
}