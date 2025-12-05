package com.example.shiftplanner.api.staff;

import java.util.List;

public record CreateStaffMemberRequest (
        String firstName,
        String lastName,
        String profession,
        Double fte,
        List<String> roles
) {}
