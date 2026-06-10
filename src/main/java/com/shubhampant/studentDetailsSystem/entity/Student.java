package com.shubhampant.studentDetailsSystem.entity;


import com.shubhampant.studentDetailsSystem.enums.Section;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

// Entity representing a student.
@Data
@Builder
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    @Indexed
    private String name;

    private LocalDate dob;

    @Indexed
    @Min(1)
    @Max(12)
    //For mass updates, increasing all the ints by 1 would be easier than updating all strings.
    // Also uses less space
    private int grade;

    @NotBlank(message = "email cannot be blank")
    @Email
    @Indexed(unique = true)
    private String email;

    @Indexed
    @NotNull(message = "section is required")
    private Section section;

    // Not indexed because these fields are rarely searched.
    // Additional indexes increase memory usage and slow inserts/updates.

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

}
