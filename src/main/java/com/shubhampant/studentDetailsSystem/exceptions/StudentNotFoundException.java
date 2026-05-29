package com.shubhampant.studentDetailsSystem.exceptions;

public class StudentNotFoundException extends RuntimeException{

    public StudentNotFoundException() {}

    public StudentNotFoundException(String msg) {
        super(msg);
    }
}
