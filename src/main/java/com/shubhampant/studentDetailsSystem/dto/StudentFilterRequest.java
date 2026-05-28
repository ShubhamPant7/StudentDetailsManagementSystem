package com.shubhampant.studentDetailsSystem.dto;
import com.shubhampant.studentDetailsSystem.enums.Section;

import lombok.Data;

@Data
public class StudentFilterRequest {
    private String name;
    private Integer grade;
    private Section section;
    private int page = 0;
    private int size = 10;
    private String sortBy = "name";
    private String direction = "asc";
}
