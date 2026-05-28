package com.shubhampant.studentDetailsSystem.entity;


import com.shubhampant.studentDetailsSystem.enums.Section;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "students-data")
public class Student {

    @Id
    private String id;

    @NotBlank
    @Indexed
    private String name;

    private LocalDate dob;

    @Indexed
    @Min(1)
    @Max(12)
    //For mass updates, increasing all the ints by 1 would be easier than updating all strings.
    // Also uses less space
    private int grade;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String email;

    @Indexed
    @NotNull
    private Section section;

    // Not indexed because these fields are rarely searched.
    // Additional indexes increase memory usage and slow inserts/updates.
    private String phoneNumber;

    private String address;

}
