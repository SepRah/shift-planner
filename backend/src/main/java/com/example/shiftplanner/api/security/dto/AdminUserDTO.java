package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.QualificationLevel;

import java.util.Set;

public record AdminUserDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        QualificationLevel staffQualificationLevel,
        double fte,
        boolean active,
        Set<UserRole> roles
) {}