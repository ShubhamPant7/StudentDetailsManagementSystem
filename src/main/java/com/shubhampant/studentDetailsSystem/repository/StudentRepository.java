package com.shubhampant.studentDetailsSystem.repository;

import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Student> findByGrade(int grade, Pageable pageable);
    Page<Student> findBySection(Section section, Pageable pageable);
    Page<Student> findByGradeAndSection(int grade, Section section, Pageable pageable);
    Page<Student> findByNameStartingWithIgnoreCase(String name, Pageable pageable);

    Page<Student> findByNameStartingWithIgnoreCaseAndGrade(
            String name,
            int grade,
            Pageable pageable
    );

    Page<Student> findByNameStartingWithIgnoreCaseAndSection(
            String name,
            Section section,
            Pageable pageable
    );

    Page<Student> findByNameStartingWithIgnoreCaseAndGradeAndSection(
            String name,
            int grade,
            Section section,
            Pageable pageable
    );
}
