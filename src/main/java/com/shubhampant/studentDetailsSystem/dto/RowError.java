package com.shubhampant.studentDetailsSystem.dto;

import lombok.Getter;

//DTO representing an error while parsing excel file (includes both row number and the error/s received).
public class RowError {

    @Getter
    private int rowNumber;

    @Getter
    private String errorMessage;

    public RowError(int rowNumber, String errorMessage) {
        this.rowNumber = rowNumber;
        this.errorMessage = errorMessage;
    }
}
