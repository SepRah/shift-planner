package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;

public record UserProfileDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        QualificationLevel qualification,
        double fte
) {}