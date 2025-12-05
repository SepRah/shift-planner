package com.example.shiftplanner.api.security.dto;

public record ChangePasswordRequestDTO(
        String oldPassword,
        String newPassword
) {}