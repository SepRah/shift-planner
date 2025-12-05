package com.example.shiftplanner.api.staff.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;

public record StaffMemberUpdateDTO(
        Long id,
        String firstName,
        String lastName,
        QualificationLevel staffQualificationLevel, // String (Enum-Name), z.B. "JUNIOR"
        Double fte
) {}
