package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.UserResponseDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberCreateDTO;
import com.example.shiftplanner.domain.security.User;


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

}