package com.example.shiftplanner.api.staff.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;

/**
 * Data Transfer Object used to return a {@link com.example.shiftplanner.domain.staff.StaffMember}
 * from the API to the client.
 * <p>
 * Characteristics:
 * <ul>
 *   <li>Sent <strong>from the server to the client</strong>.</li>
 *   <li>Contains only externally visible, API-safe values.</li>
 *   <li>Always includes the generated {@code id}.</li>
 *   <li>Does not expose internal domain structures such as value objects.</li>
 * </ul>
 * <p>
 * This DTO is produced by {@code StaffMemberMapper.toDto()} and typically used in
 * GET, POST, PATCH, and DELETE responses.
 * @author Sina Enzmann
 * @version 1.0
 * @since 2025-12-07
 */
public record StaffMemberResponseDTO(
        Long id,
        String firstName,
        String lastName,
        QualificationLevel staffQualificationLevel, // String (Enum-Name), z.B. "JUNIOR"
        Double fte
) {}
