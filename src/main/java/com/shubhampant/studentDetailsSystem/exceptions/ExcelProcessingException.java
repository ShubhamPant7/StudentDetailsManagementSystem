package com.shubhampant.studentDetailsSystem.exceptions;

public class ExcelProcessingException extends RuntimeException{
    public ExcelProcessingException() {}

    public ExcelProcessingException(String message) {
        super(message);
    }
}
