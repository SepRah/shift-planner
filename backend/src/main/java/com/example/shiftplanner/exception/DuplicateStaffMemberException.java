package com.example.shiftplanner.exception;

public class DuplicateStaffMemberException extends RuntimeException {
    public DuplicateStaffMemberException(String firstName, String lastName) {
        super("StaffMember with name '" + firstName + " " + lastName + "' already exists");
    }
}
