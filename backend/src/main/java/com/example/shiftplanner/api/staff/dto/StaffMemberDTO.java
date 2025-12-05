package com.example.shiftplanner.api.staff.dto;

import java.util.List;

public record StaffMemberDTO (
        String id,
        String firstName,
        String lastName,
        String role,      // String (Enum-Name), z.B. "NURSE"
        List<String> roles      // "ADMIN", "SUPERVISOR", ...
) {}
