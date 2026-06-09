package com.shubhampant.studentDetailsSystem.Exceptions;

import com.shubhampant.studentDetailsSystem.exceptions.AuditNotFoundException;
import com.shubhampant.studentDetailsSystem.exceptions.ExcelProcessingException;
import com.shubhampant.studentDetailsSystem.exceptions.GlobalExceptionHandler;
import com.shubhampant.studentDetailsSystem.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleStudentNotFoundException() {

        StudentNotFoundException exception = new StudentNotFoundException("Student not found");

        ResponseEntity<String> response = handler.handleStudentNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertEquals("Error: Student not found", response.getBody());
    }

    @Test
    void testHandleAuditNotFoundException() {

        AuditNotFoundException exception = new AuditNotFoundException("Audit not found");

        ResponseEntity<String> response = handler.handleAuditRecordNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertEquals("Audit not found", response.getBody());
    }

    @Test
    void testHandleExcelProcessingException() {

        ExcelProcessingException exception = new ExcelProcessingException("Failed to parse Excel file");

        ResponseEntity<String> response = handler.handleExcelProcessingException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Error: Failed to parse Excel file", response.getBody());
    }

    @Test
    void testHandleDuplicateKeyException() {

        DuplicateKeyException exception = new DuplicateKeyException("duplicate");

        ResponseEntity<String> response = handler.handleDuplicateKeyException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        assertEquals("Error: Email already exists", response.getBody());
    }

    @Test
    void testHandleInvalidDateFormat() {

        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Cannot deserialize LocalDate");

        ResponseEntity<String> response = handler.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Invalid date format. Use yyyy-MM-dd", response.getBody());
    }

    @Test
    void testHandleInvalidSection() {

        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Cannot deserialize Section");

        ResponseEntity<String> response = handler.handleHttpMessageNotReadableException(exception);

        assertEquals("Invalid section. Allowed values: A, B, C, D", response.getBody());
    }

    @Test
    void testHandleInvalidRequestBody() {

        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Random parsing error");

        ResponseEntity<String> response = handler.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Invalid request body", response.getBody());
    }
}
