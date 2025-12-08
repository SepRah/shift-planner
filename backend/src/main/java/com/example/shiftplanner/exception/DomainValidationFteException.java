package com.example.shiftplanner.exception;

public class DomainValidationFteException extends RuntimeException {
    public DomainValidationFteException() {
        super("FTE must be between 0.0 and 1.0");
    }
}
