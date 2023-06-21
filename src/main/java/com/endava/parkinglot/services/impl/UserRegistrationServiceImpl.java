package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.UserRegistrationDtoResponse;
import com.endava.parkinglot.exceptions.FailedEmailNotificationException;
import com.endava.parkinglot.exceptions.UserNotFoundException;
import com.endava.parkinglot.exceptions.UserNotGrantedToDoActionException;
import com.endava.parkinglot.exceptions.ValidationCustomException;
import com.endava.parkinglot.mapper.UserMapper;
import com.endava.parkinglot.model.Role;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import com.endava.parkinglot.services.EmailNotificationService;
import com.endava.parkinglot.services.UserRegistrationService;
import jakarta.transaction.Transactional;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final EmailNotificationService emailNotificationService;

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    @Autowired
    public UserRegistrationServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, EmailNotificationService emailNotificationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest) {
        UserEntity user;
        user = userMapper.mapRequestDtoToEntity(registrationDtoRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.REGULAR);

        return userMapper.mapEntityToResponseDto(userRepository.save(user));
    }

    private String getRole() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        String role = "";
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            role = authority.toString();
        }
        role = role.substring(5);

        return role;
    }

    @Transactional
    private UserEntity grantAdminPermissionsById(Long userId) {
        if (getRole().equals(Role.ADMIN.toString())) {
            UserEntity user = userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException("User with ID " + userId + " not found.")
            );
            user.setRole(Role.ADMIN);
            return userRepository.save(user);
        } else {
            throw new UserNotGrantedToDoActionException("User doesn't have authorities to do this action");
        }
    }

    @Transactional
    private UserEntity grantAdminPermissionsByEmail(String email) {
        if (getRole().equals(Role.ADMIN.toString())) {
            UserEntity user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UserNotFoundException("User with email " + email + " not found.")
            );
            user.setRole(Role.ADMIN);
            return userRepository.save(user);
        } else {
            throw new UserNotGrantedToDoActionException("User doesn't have authorities to do this action");
        }
    }

    private void validateEmail(String email) {
        if (email != null) {
            EmailValidator validator = EmailValidator.getInstance();
            if (!validator.isValid(email)) {
                Map<String, String> errors = new HashMap<>();
                errors.put("email", "Invalid email. It should be like: 'example@email.com'");
                throw new ValidationCustomException(errors);
            }
        }
    }

    @Override
    public void grantAdminPermissions(Long id, String email) {
        validateEmail(email);
        UserEntity entity;
        if (id != null) {
            entity = grantAdminPermissionsById(id);
        } else if (email != null) {
            entity = grantAdminPermissionsByEmail(email);
        } else {
            throw new UserNotFoundException("Missing user ID or email.");
        }
        try {
            emailNotificationService.sendNotificationAboutGrantedAdminRole(entity.getEmail());
        } catch (FailedEmailNotificationException failedEmailNotificationException) {
            logger.warn("Email was not sent, admin permissions granted for user: " + entity.getEmail());
        }

    }
}
