package com.shubhampant.studentDetailsSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExcelProcessingException.class)
    public ResponseEntity<String> handleExcelProcessingException(ExcelProcessingException ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidateExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException ex) {
        return new ResponseEntity<>("Error: Email already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String>
    handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {
        if (ex.getMessage().contains("LocalDate")) {
            return new ResponseEntity<>(
                    "Invalid date format. Use yyyy-MM-dd",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (ex.getMessage().contains("Section")) {
            return new ResponseEntity<>(
                    "Invalid section. Allowed values: A, B, C, D",
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                "Invalid request body",
                HttpStatus.BAD_REQUEST
        );
    }
}
