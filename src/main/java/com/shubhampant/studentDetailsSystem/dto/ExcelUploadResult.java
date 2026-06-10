package com.shubhampant.studentDetailsSystem.dto;

import com.shubhampant.studentDetailsSystem.entity.Student;
import lombok.Getter;

import java.util.List;

//DTO representing result of uploading the excel file.
public class ExcelUploadResult {

    @Getter
    private List<Student> validStudents;

    @Getter
    private List<RowError> errors;

    public ExcelUploadResult(List<Student> validStudents, List<RowError> errors) {
        this.validStudents = validStudents;
        this.errors = errors;
    }
}
