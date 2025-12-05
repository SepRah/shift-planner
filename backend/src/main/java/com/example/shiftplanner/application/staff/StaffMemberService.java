package com.example.shiftplanner.application.staff;

import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.api.staff.CreateStaffMemberRequest;
import java.util.List;


public interface  StaffMemberService {
    StaffMemberDTO createStaffMember (CreateStaffMemberRequest request);
    List<StaffMemberDTO> getAll();
    StaffMemberDTO getById(String id);
    List<StaffMemberDTO> getByProfession(String profession);
    void delete(String id);
}
