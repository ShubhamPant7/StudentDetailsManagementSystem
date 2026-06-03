package com.shubhampant.studentDetailsSystem.repository;

import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import org.springframework.data.domain.Page;

public interface StudentRepositoryCustom {
    Page<Student> filterStudents(StudentFilterRequest request);
}
