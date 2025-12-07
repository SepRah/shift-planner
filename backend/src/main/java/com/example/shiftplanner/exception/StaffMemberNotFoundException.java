package com.example.shiftplanner.exception;

public class StaffMemberNotFoundException extends RuntimeException {
    public StaffMemberNotFoundException() {
        super("Staff member not found");
    }
}
