package com.example.shiftplanner.api.security;

import com.example.shiftplanner.domain.task.QualificationLevel;

public record UserRegistrationRequestDTO(
        String username,
        String password,
        String firstName,
        String lastName,
        QualificationLevel qualification
) {}
