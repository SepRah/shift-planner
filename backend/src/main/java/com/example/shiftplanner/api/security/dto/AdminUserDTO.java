package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.domain.security.UserRole;

import java.util.Set;

public record AdminUserDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        boolean active,
        Set<UserRole> roles
) {}