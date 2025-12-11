package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.api.security.dto.UserResponseDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberCreateDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.StaffMember;

import java.util.Set;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRoles(),
                new StaffMemberCreateDTO(
                        user.getStaffmember().getName().getFirstName(),
                        user.getStaffmember().getName().getLastName(),
                        user.getStaffmember().getStaffQualificationLevel(),
                        user.getStaffmember().getFte()
                )
        );
    }

    public static User toEntity(UserRegistrationRequestDTO dto, String encodedPassword) {
        StaffMember staff = StaffMember.create(dto.firstName(), dto.lastName(), dto.qualification(), dto.fte());
        return User.create(dto.username(), encodedPassword, Set.of(UserRole.USER), staff);
    }
}