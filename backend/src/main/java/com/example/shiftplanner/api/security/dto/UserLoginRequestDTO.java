package com.example.shiftplanner.api.security.dto;

public record UserLoginRequestDTO(
        String username,
        String password
) {}