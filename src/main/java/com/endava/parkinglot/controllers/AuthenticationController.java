package com.endava.parkinglot.controllers;

import com.endava.parkinglot.DTO.AuthenticationDTO;
import com.endava.parkinglot.security.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken inputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getEmail(), authenticationDTO.getPassword());

        authenticationManager.authenticate(inputToken);

        String jwt = jwtUtil.generateAccessToken(authenticationDTO.getEmail());

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
