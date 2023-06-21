package com.endava.parkinglot.services.impl;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.UserRegistrationDtoResponse;
import com.endava.parkinglot.mapper.UserMapper;
import com.endava.parkinglot.model.Role;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import com.endava.parkinglot.services.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest) {
        UserEntity user;
        user = userMapper.mapRequestDtoToEntity(registrationDtoRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.REGULAR);

        userRepository.save(user);

        return null;
    }

    @Override
    public void grantAdminPermissions(Long id) {

    }
}
