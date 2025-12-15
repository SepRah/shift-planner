package com.example.shiftplanner.api.security.dto;

public record UserRegistrationRequestDTO(
        String username,
        String password,
        String firstName,
        String lastName,
        double fte
) {}
