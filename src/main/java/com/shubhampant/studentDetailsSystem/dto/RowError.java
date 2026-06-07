package com.shubhampant.studentDetailsSystem.dto;

import lombok.Getter;

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
