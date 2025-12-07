package com.example.shiftplanner.api.staff.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;

/**
 * Data Transfer Object used for partially updating an existing
 * {@link com.example.shiftplanner.domain.staff.StaffMember}.
 * <p>
 * Usage:
 * <ul>
 *   <li>Sent <strong>from the client to the server</strong> during an update request.</li>
 *   <li>Supports <strong>partial updates</strong> (PATCH semantics):
 *       only non-null fields are applied to the entity.</li>
 * </ul>
 * <p>
 * Design notes:
 * <ul>
 *   <li>No {@code id} field — the resource ID is taken from the URL path:
 *       {@code PATCH /api/staffmembers/{id}}.</li>
 *   <li>All fields are optional; {@code null} means “do not modify this field”.</li>
 *   <li>Domain constraints (e.g., name validity, FTE range) are enforced in the entity.</li>
 * </ul>
 */
public record StaffMemberUpdateDTO(
        String firstName,
        String lastName,
        QualificationLevel staffQualificationLevel, // String (Enum-Name), z.B. "JUNIOR"
        Double fte
) {}
