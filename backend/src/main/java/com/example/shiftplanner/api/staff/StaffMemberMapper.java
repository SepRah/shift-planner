package com.example.shiftplanner.api.staff;

import com.example.shiftplanner.application.staff.*;
import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.domain.staff.*;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StaffMemberMapper {

    public static StaffMember toDomain(
            CreateStaffMemberRequest req,
            List<Role> availableRoles
    ) {
        Name name = new Name(req.firstName(), req.lastName());
        Role role = Role.valueOf(req.profession());
        double fte = req.fte() == null ? 1.0 : req.fte();

        validateFte(fte);

        Set<Role> roles = mapRoleNamesToRoles(req.roles(), availableRoles);

        return new StaffMember(name, role, fte, roles);
    }

    public static StaffMemberDTO toDto(StaffMember staff) {
        List<String> roleNames = staff.getRoles().stream()
                .map(Role::getName)
                .toList();

        return new StaffMemberDTO(
                staff.getId(),
                staff.getName().getFirstName(),
                staff.getName().getLastName(),
                staff.getRole().getName(),
                staff.getFte(),
                roleNames
        );
    }

    private static void validateFte(double fte) {
        if (fte < 0.0 || fte > 1.0) {
            throw new IllegalArgumentException("FTE must be between 0.0 and 1.0");
        }
    }

    private static Set<Role> mapRoleNamesToRoles(List<String> names, List<Role> availableRoles) {
        if (names == null) return Set.of();
        return availableRoles.stream()
                .filter(r -> names.contains(r.getName()))
                .collect(Collectors.toSet());
    }
}

