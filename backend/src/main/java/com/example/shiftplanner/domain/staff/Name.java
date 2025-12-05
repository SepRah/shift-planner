package com.example.shiftplanner.domain.staff;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Name {
    private String firstName;
    private String lastName;

    protected Name() {}

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

    // Getters only – immutability
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() {
        return firstName + " " + lastName;
    }
}