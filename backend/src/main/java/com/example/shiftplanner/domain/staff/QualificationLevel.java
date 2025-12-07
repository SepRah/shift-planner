package com.example.shiftplanner.domain.staff;
/**
 * Enumeration representing the qualification level of a staff member.
 * <p>
 * This value is used to:
 * <ul>
 *   <li>classify staff members by their experience or role,</li>
 *   <li>define which staff members are allowed to work on which tasks.</li>
 * </ul>
 */
public enum QualificationLevel {
    NONE,           // Nothing required
    MANAGER,        // Manager Level
    SENIOR,         // Senior Level
    JUNIOR          // Junior Level
}