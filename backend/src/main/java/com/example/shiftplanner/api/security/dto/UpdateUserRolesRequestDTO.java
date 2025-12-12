package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.domain.security.UserRole;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdateUserRolesRequestDTO(
        @NotEmpty Set<UserRole> roles
) {}
