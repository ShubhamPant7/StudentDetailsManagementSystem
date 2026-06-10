package com.shubhampant.studentDetailsSystem.exceptions;

//Custom exception class for any errors when processing Excel files (bulk upload/download)
public class ExcelProcessingException extends RuntimeException{
    public ExcelProcessingException() {}

    public ExcelProcessingException(String message) {
        super(message);
    }
}
