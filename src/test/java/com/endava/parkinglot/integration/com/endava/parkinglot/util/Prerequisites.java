package com.endava.parkinglot.integration.com.endava.parkinglot.util;

import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Prerequisites {

    private final UserRepository userRepository;
    @Transactional
    public void clear() {
        userRepository.deleteAll();
    }
    @Transactional
    public void createUser() {
        UserEntity user = UserEntity.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .build();
        userRepository.save(user);
    }


}