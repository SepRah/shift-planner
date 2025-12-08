package com.example.shiftplanner.api.staff;

import com.example.shiftplanner.api.staff.dto.*;
import com.example.shiftplanner.domain.staff.*;

/**
 * Mapper for converting between StaffMember domain entities and DTOs.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Creates {@link StaffMember} entities from incoming create DTOs.</li>
 *   <li>Converts {@link StaffMember} entities to response DTOs for API output.</li>
 * </ul>
 * <p>
 * This class does not contain business logic – only mapping logic.
 */
public class StaffMemberMapper {
    /**
     * Maps a {@link StaffMemberCreateDTO} to a new {@link StaffMember} entity.
     * <p>
     * Domain validation (e.g. name not null, FTE range) is delegated
     * to the {@link Name} and {@link StaffMember} constructors.
     *
     * @param dtoCreate DTO containing the data to create a new staff member
     * @return a new {@link StaffMember} entity
     */
    public static StaffMember toEntity(StaffMemberCreateDTO dtoCreate) {
        Name name = new Name(dtoCreate.firstName(), dtoCreate.lastName());
        QualificationLevel staffQualificationLevel = dtoCreate.staffQualificationLevel();
        double fte = dtoCreate.fte();

        return new StaffMember (name, staffQualificationLevel, fte);
    }

    /**
     * Maps a {@link StaffMember} entity to a {@link StaffMemberResponseDTO}.
     *
     * @param staffMember the entity that should be converted
     * @return a DTO representation of the given staff member
     */
    public static StaffMemberResponseDTO toDto(StaffMember staffMember) {

        return new StaffMemberResponseDTO(
                staffMember.getId(),
                staffMember.getName().getFirstName(),
                staffMember.getName().getLastName(),
                staffMember.getStaffQualificationLevel(),
                staffMember.getFte()
        );
    }
}

