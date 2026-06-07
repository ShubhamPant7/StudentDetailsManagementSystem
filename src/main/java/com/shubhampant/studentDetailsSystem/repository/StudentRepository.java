package com.shubhampant.studentDetailsSystem.repository;

import com.shubhampant.studentDetailsSystem.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String>, StudentRepositoryCustom {
    boolean existsByEmail(String email);
}
