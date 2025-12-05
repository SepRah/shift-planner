package com.example.shiftplanner.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException() {
        super("Assignment not found");
    }
}
