package com.endava.parkinglot.integration.com.endava.parkinglot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.endava.parkinglot.enums.Role;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class Prerequisites {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret.custom_jwt_secret}")
    private String SECRET;

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

    @Transactional
    public UserEntity createUserAdmin() {
        String password = "Jonathan_2";
        UserEntity user = UserEntity.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .password(passwordEncoder.encode(password.trim()))
                .phone("068112233")
                .role(Role.ADMIN)
                .build();
        return userRepository.save(user);
    }

    public String generateAccessToken(String username, String role){
        Date expirationDate = Date.from(ZonedDateTime.now().plusHours(24).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer("PARKING_LOT")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET));
    }


}