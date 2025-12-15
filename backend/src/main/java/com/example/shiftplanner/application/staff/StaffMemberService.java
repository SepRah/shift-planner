package com.example.shiftplanner.application.staff;

import com.example.shiftplanner.api.staff.StaffMemberMapper;
import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.api.staff.dto.StaffMemberUpdateDTO;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.exception.DuplicateStaffMemberException;
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
        if (staffMemberRepository.existsByNameFirstNameAndNameLastName(staffMemberCreateDTO.firstName(), staffMemberCreateDTO.lastName())){
            throw new DuplicateStaffMemberException(staffMemberCreateDTO.firstName(), staffMemberCreateDTO.lastName());
        }
        StaffMember staffMember = StaffMemberMapper.toEntity(staffMemberCreateDTO);
        staffMember = staffMemberRepository.save(staffMember);
        return StaffMemberMapper.toDto(staffMember);
    }

    /**
     * Updates an existing {@link StaffMember} by applying the changes provided in the
     * {@link StaffMemberUpdateDTO}. This method performs a <strong>partial update</strong>:
     * only fields explicitly set (non-null) in the DTO are modified, while all other fields
     * remain unchanged.
     *
     * <p><strong>Update workflow:</strong></p>
     * <ol>
     *   <li><strong>Entity lookup:</strong>
     *       The staff member is loaded by ID. If no matching entity is found,
     *       a {@link StaffMemberNotFoundException} is thrown.</li>
     *
     *   <li><strong>Name update (optional):</strong>
     *       If either {@code firstName} or {@code lastName} is provided,
     *       a new {@link Name} value object is constructed.
     *       Missing values are taken from the existing entity.
     *       <br>
     *       <em>Note:</em> Value objects are replaced instead of mutated, following DDD principles.</li>
     *
     *   <li><strong>QualificationLevel update (optional):</strong>
     *       Applied only when provided. The setter invokes domain-level validation where applicable.</li>
     *
     *   <li><strong>FTE update (optional):</strong>
     *       Applied only when provided. The domain model enforces invariants
     *       (FTE must be between 0.0 and 1.0).</li>
     *
     *   <li><strong>Persistence:</strong>
     *       The updated entity is saved via the repository.</li>
     *
     *   <li><strong>DTO conversion:</strong>
     *       The saved entity is converted into a {@link StaffMemberResponseDTO}
     *       for API output.</li>
     * </ol>
     *
     * @param id   the ID of the staff member to update
     * @param dto  DTO containing update values; only non-null fields are applied
     * @return a {@link StaffMemberResponseDTO} representing the updated staff member
     *
     * @throws StaffMemberNotFoundException if no staff member with the given ID exists
     * @throws IllegalArgumentException     if domain invariants are violated
     *                                      (e.g. invalid name or FTE outside 0.0–1.0)
     */
    public StaffMemberResponseDTO updateStaffMember(Long id, StaffMemberUpdateDTO dto) {
        StaffMember staffMember = staffMemberRepository.findStaffMemberById(id)
                .orElseThrow(StaffMemberNotFoundException::new);

        // Name
        if (dto.firstName() != null || dto.lastName() != null) {
            String newFirst = dto.firstName() != null ? dto.firstName() : staffMember.getName().getFirstName();
            String newLast  = dto.lastName()  != null ? dto.lastName()  : staffMember.getName().getLastName();
            staffMember.setName(new Name(newFirst, newLast));
        }
        // QualificationLevel
        if (dto.staffQualificationLevel() != null) {
            staffMember.setStaffQualificationLevel(dto.staffQualificationLevel());
        }
        // FTE
        if (dto.fte() != null) {
            staffMember.setFte(dto.fte()); // Domain validiert
        }

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
