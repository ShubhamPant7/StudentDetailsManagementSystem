package com.shubhampant.studentDetailsSystem.exceptions;

//Custom exception class for when a Student is not found.
public class StudentNotFoundException extends RuntimeException{

    public StudentNotFoundException() {}

    public StudentNotFoundException(String msg) { super(msg); }
}
