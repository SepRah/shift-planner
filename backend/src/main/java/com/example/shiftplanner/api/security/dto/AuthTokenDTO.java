package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.domain.security.UserRole;

import java.util.Set;

public record AuthTokenDTO(
        String token,
        String username,
        Set<UserRole> roles
) {}
