package com.example.shiftplanner.exception;

public class DomainValidationNotNullException extends RuntimeException {
    public DomainValidationNotNullException (String field) {
        super(field + "is required");
    }
}
