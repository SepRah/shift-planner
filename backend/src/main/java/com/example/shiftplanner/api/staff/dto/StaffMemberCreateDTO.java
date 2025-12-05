package com.example.shiftplanner.api.staff.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import jakarta.validation.constraints.NotBlank;

public record StaffMemberCreateDTO(
        @NotBlank
        Long id,
        String firstName,
        String lastName,
        QualificationLevel staffQualificationLevel,    // String (Enum-Name), z.B. "JUNIOR"
        Double fte
) {}
