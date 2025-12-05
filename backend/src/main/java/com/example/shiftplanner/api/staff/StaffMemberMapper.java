package com.example.shiftplanner.api.staff;

import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.domain.staff.*;

public class StaffMemberMapper {

    public static StaffMember toDomain(
            StaffMemberUpdateDTO req
    ) {
        Name name = new Name(req.firstName(), req.lastName());
        QualificationLevel staffQualificationLevel = req.staffQualificationLevel();
        double fte = req.fte();

        return new StaffMember(name, staffQualificationLevel, fte);
    }

    public static StaffMemberCreateDTO toDto(StaffMember staffMember) {

        return new StaffMemberCreateDTO(
                staffMember.getId(),
                staffMember.getName().getFirstName(),
                staffMember.getName().getLastName(),
                staffMember.getStaffQualificationLevel(),
                staffMember.getFte()
        );
    }
}

