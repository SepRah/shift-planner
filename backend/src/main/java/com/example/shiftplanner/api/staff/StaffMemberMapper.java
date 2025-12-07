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

    /**
     * Applies the changes from a {@link StaffMemberUpdateDTO} to an existing
     * {@link StaffMember} domain entity.
     * <p>
     * This method performs a <strong>partial update</strong>: only non-null fields
     * in the update DTO are applied to the target entity. Fields that are {@code null}
     * in the DTO remain unchanged.
     * <p>
     * Update behaviour:
     * <ul>
     *   <li><strong>Name</strong> – If either {@code firstName} or {@code lastName} is provided,
     *       a new {@link Name} value object is created. Missing fields are taken from the
     *       existing name. Value objects are replaced rather than mutated to follow DDD principles.</li>
     *   <li><strong>QualificationLevel</strong> – Updated only if the DTO specifies a non-null value.</li>
     *   <li><strong>FTE</strong> – Updated only if non-null. The domain setter validates the
     *       allowed range (0.0 to 1.0).</li>
     * </ul>
     *
     * @param staffMember the existing domain entity that should be updated
     * @param updateDTO   the DTO containing the fields to update; any field set to {@code null}
     *                    will be ignored and therefore remain unchanged
     *
     * @throws IllegalArgumentException if the new name or FTE violates domain invariants
     */
    public static void applyUpdate(StaffMember staffMember, StaffMemberUpdateDTO updateDTO) {

        // updating Name (if one of the two values are not empty)
        if (updateDTO.firstName() != null || updateDTO.lastName() != null) {
            String newFirst = updateDTO.firstName() != null
                    ? updateDTO.firstName()
                    : staffMember.getName().getFirstName();

            String newLast = updateDTO.lastName() != null
                    ? updateDTO.lastName()
                    : staffMember.getName().getLastName();

            // Name-Value-Object macht eigene Validierung
            staffMember.setName(new Name(newFirst, newLast));
        }

        // Updating QualificationLevel
        if (updateDTO.staffQualificationLevel() != null) {
            staffMember.setStaffQualificationLevel(updateDTO.staffQualificationLevel());
        }

        // Updating FTE  (Domain-Setter validates 0.0–1.0 range)
        if (updateDTO.fte() != null) {
            staffMember.setFte(updateDTO.fte());
        }
    }
}

