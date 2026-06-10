package com.shubhampant.studentDetailsSystem.exceptions;

//Custom exception class for when an Audit is not found.
public class AuditNotFoundException extends RuntimeException {
    public AuditNotFoundException() {};

    public AuditNotFoundException(String message) {
        super(message);
    }
}
