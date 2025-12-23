package com.example.shiftplanner.api.security.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegistrationRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("1.0")
        double fte
) {}
