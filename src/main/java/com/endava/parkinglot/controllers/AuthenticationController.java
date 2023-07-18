package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.auth.AuthAndRegistrationResponseDTO;
import com.endava.parkinglot.DTO.auth.AuthenticationDTO;
import com.endava.parkinglot.security.JWTUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    public AuthenticationController(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<AuthAndRegistrationResponseDTO> authenticate(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        authenticationDTO.setEmail(authenticationDTO.getEmail().toLowerCase());

        logger.info("Trying to authenticate user with username: " + authenticationDTO.getEmail());

        String role = "";
        UsernamePasswordAuthenticationToken inputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getEmail(), authenticationDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(inputToken);

        logger.info("Provided valid credentials.");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        for(GrantedAuthority authority : userDetails.getAuthorities()){
            role = authority.getAuthority().substring(5);
        }

        String jwt = jwtUtil.generateAccessToken(authenticationDTO.getEmail());

        AuthAndRegistrationResponseDTO response = new AuthAndRegistrationResponseDTO(
                userDetails.getUsername(),
                role,
                jwt
        );

        logger.info("User is successfully authenticated.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
