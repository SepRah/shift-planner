package com.example.shiftplanner.application.staff;

import com.example.shiftplanner.api.staff.StaffMemberMapper;
import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.api.staff.dto.StaffMemberUpdateDTO;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.exception.StaffMemberNotFoundException;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import org.springframework.stereotype.Service;

/**
 * Application service for handling use cases related to {@link StaffMember}.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Coordinates creation, update and retrieval of staff members.</li>
 *   <li>Uses the {@link StaffMemberRepository} to access persistence.</li>
 *   <li>Maps between domain entities and DTOs via {@link StaffMemberMapper}.</li>
 *   <li>Throws {@link StaffMemberNotFoundException} when requested entities do not exist.</li>
 * </ul>
 * <p>
 * This service contains application-level logic, but no HTTP or persistence details.
 */
@Service
public class  StaffMemberService {

    private final StaffMemberRepository staffMemberRepository;

    public StaffMemberService (StaffMemberRepository staffMemberRepository) {
        this.staffMemberRepository = staffMemberRepository;
    }

    /**
     * Creates a new staff member in the system.
     *
     * @param staffMemberCreateDTO data required to create the staff member
     * @return a DTO representing the newly created staff member
     */
    public StaffMemberResponseDTO create(StaffMemberCreateDTO staffMemberCreateDTO) {
        StaffMember staffMember = StaffMemberMapper.toEntity(staffMemberCreateDTO);
        staffMember = staffMemberRepository.save(staffMember);
        return StaffMemberMapper.toDto(staffMember);
    }

    /**
     * Updates an existing {@link StaffMember} by applying the changes provided in
     * the {@link StaffMemberUpdateDTO}. This method performs a <strong>partial update</strong>,
     * meaning that only fields explicitly set in the DTO are modified. Fields set to
     * {@code null} remain unchanged.
     * <p>
     * Update process:
     * <ol>
     *   <li>Load the existing staff member by ID. If no entity is found,
     *       a {@link StaffMemberNotFoundException} is thrown.</li>
     *   <li>Apply non-null fields from the update DTO using
     *       {@link StaffMemberMapper#applyUpdate(StaffMember, StaffMemberUpdateDTO)}.
     *       This ensures that domain rules are respected, e.g.:</li>
     *       <ul>
     *         <li>the {@link Name} value object is replaced rather than mutated,</li>
     *         <li>the qualification level is updated only when provided,</li>
     *         <li>FTE updates are validated in the domain model.</li>
     *       </ul>
     *   <li>Persist the updated staff member using the repository.</li>
     *   <li>Convert the updated entity into a response DTO via
     *       {@link StaffMemberMapper#toDto(StaffMember)}.</li>
     * </ol>
     *
     * @param id                    ID of the staff member to update
     * @param staffMemberUpdateDTO  DTO containing the fields to update; only non-null
     *                              values are applied
     * @return {@link StaffMemberResponseDTO} representation of the updated staff member
     *
     * @throws StaffMemberNotFoundException if no staff member with the given ID exists
     * @throws IllegalArgumentException     if domain-level invariants are violated (e.g. invalid FTE or name)
     */
    public StaffMemberResponseDTO updateStaffMember(Long id, StaffMemberUpdateDTO staffMemberUpdateDTO) {
        StaffMember staffMember = staffMemberRepository.findStaffMemberById(id)
                .orElseThrow(StaffMemberNotFoundException::new);
        // apply fields from DTO
        StaffMemberMapper.applyUpdate(staffMember, staffMemberUpdateDTO);
        // save
        StaffMember saved = staffMemberRepository.save(staffMember);
        return StaffMemberMapper.toDto(saved);
    }

    /**
     * Deletes a staff member by ID.
     * @param id ID of the staff member that should be deleted
     * @throws StaffMemberNotFoundException if no staff member with this ID exists
     */
    public void deleteStaffMember(Long id) {
        boolean exists = staffMemberRepository.existsStaffMemberById(id);
        if (!exists) {
            throw new StaffMemberNotFoundException();
        }

        staffMemberRepository.deleteById(id);
    }

    /**
     * Returns staff member according to the ID.
     * @param id ID of staff member which should be returned
     * @return a DTO representing the requested staff member
     * @throws StaffMemberNotFoundException if no staff member with the given ID exists
     */
    public StaffMemberResponseDTO getStaffMemberById(Long id){
        StaffMember staffMember = staffMemberRepository.findStaffMemberById(id)
                .orElseThrow(StaffMemberNotFoundException::new);
        return StaffMemberMapper.toDto(staffMember);
    }

    /**
     * Returns staff member by its full name (first name and last name).
     * @param firstName firstname of staff member
     * @param lastName firstname of staff member
     * @return a DTO representing the requested staff member
     * @throws StaffMemberNotFoundException if no staff member with the given name exists
     */
    public StaffMemberResponseDTO getStaffMemberByFullName(String firstName, String lastName){
        StaffMember staffMember = staffMemberRepository
                .findByNameFirstNameAndNameLastName(firstName, lastName)
                .orElseThrow(StaffMemberNotFoundException::new);
        return StaffMemberMapper.toDto(staffMember);
    }

}
