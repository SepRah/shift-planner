package com.example.shiftplanner.api.security.dto;

import com.example.shiftplanner.api.staff.dto.StaffMemberDTO;
import com.example.shiftplanner.domain.security.UserRole;

import java.util.Set;

public record UserResponseDTO(
        Long id,
        String username,
        Set<UserRole> roles,
        StaffMemberDTO staffmember
) {}