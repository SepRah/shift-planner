package com.example.shiftplanner.api.security;

public record ChangePasswordRequestDTO(
        String oldPassword,
        String newPassword
) {}