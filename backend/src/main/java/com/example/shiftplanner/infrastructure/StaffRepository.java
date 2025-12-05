package com.example.shiftplanner.infrastructure;

import com.example.shiftplanner.domain.staff.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffMember, Long> {

    Optional<StaffMember> findStaffMemberById(Long id);

    Optional<StaffMember> findStaffMemberByUsername(String username);

    boolean existsStaffMemberById(Long id);
}
