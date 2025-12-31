package com.example.shiftplanner.api.staff.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object used for creating a new {@link com.example.shiftplanner.domain.staff.StaffMember}.
 * <p>
 * Usage:
 * <ul>
 *   <li>Sent <strong>from the client to the server</strong> when a new staff member is created.</li>
 *   <li>Mapped to a domain entity using the StaffMemberMapper.</li>
 * </ul>
 * <p>
 * Design notes:
 * <ul>
 *   <li>No {@code id} field is included, because the database generates the ID.</li>
 *   <li>Validation annotations ensure that required input is present.</li>
 *   <li>{@code fte} may be {@code null}; domain or service logic may apply defaults or validate range (0.0–1.0).</li>
 * </ul>
 * @author Sina Enzmann
 * @version 1.0
 * @since 2025-12-07
 */
public record StaffMemberCreateDTO(
        @NotNull
        @NotBlank (message = "First name is required")
        String firstName,

        @NotNull
        @NotBlank (message = "Last name is required")
        String lastName,

        QualificationLevel staffQualificationLevel,    // String (Enum-Name), z.B. "JUNIOR"

        Double fte // may be null; domain validation enforces 0.0-1.0 range
) {}
