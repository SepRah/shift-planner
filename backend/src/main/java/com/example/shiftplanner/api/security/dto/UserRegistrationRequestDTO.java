package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;

public record UserRegistrationRequestDTO(
        String username,
        String password,
        String firstName,
        String lastName,
        QualificationLevel qualification
) {}
