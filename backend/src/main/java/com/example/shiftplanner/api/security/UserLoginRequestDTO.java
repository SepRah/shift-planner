package com.example.shiftplanner.api.security;

public record UserLoginRequestDTO(
        String username,
        String password
) {}