package com.example.shiftplanner.domain.staff;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * Value object representing a person's name.
 * <p>
 * This class is embedded in {@link StaffMember} as a JPA {@link Embeddable}
 * and contains first name and last name.
 * <p>
 * Invariants:
 * <ul>
 *   <li>firstName must not be null or blank.</li>
 *   <li>lastName must not be null or blank.</li>
 * </ul>
 */
@Getter
@Embeddable
public class Name {

    @Getter
    private String firstName;
    @Getter
    private String lastName;

    /**
     * Default constructor required by JPA.
     * Should not be used directly in application code.
     */
    protected Name() {}

    /**
     * Creates a new {@code Name} value object.
     *
     * @param firstName first name, must not be blank
     * @param lastName  last name, must not be blank
     * @throws IllegalArgumentException if either firstName or lastName is blank
     */
    public Name(String firstName, String lastName) {
        if (firstName.isBlank() || firstName.isEmpty() ) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName.isEmpty() || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }
}