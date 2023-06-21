package com.endava.parkinglot.DTO.auth;

public class AuthAndRegistrationResponseDTO {

    private final String email;
    private final String role;
    private final String jwt;

    public AuthAndRegistrationResponseDTO(String email, String role, String jwt) {
        this.email = email;
        this.role = role;
        this.jwt = jwt;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getJwt() {
        return jwt;
    }
}
