package com.example.shiftplanner.application.staff;

import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.api.staff.dto.StaffMemberUpdateDTO;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.infrastructure.StaffRepository;

import java.util.List;


public class  StaffMemberService {

    private final StaffRepository staffRepository;

    public StaffMemberService (StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    /*
    StaffMemberCreateDTO createStaffMember (StaffMemberUpdateDTO request);
    List<StaffMemberCreateDTO> getAll();
    StaffMemberCreateDTO getById(String id);
    List<StaffMemberCreateDTO> getByProfession(String profession);
    void delete(String id);
    */
}
