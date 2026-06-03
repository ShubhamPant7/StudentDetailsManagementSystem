package com.shubhampant.studentDetailsSystem.repository;

import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public StudentRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<Student> filterStudents(StudentFilterRequest request) {

        Query query = new Query();

        if (request.getName() != null && !request.getName().isBlank()) { query.addCriteria( Criteria.where("name").regex("^" + request.getName(), "i"));
        }

        if (request.getGrade() != null) { query.addCriteria( Criteria.where("grade").is(request.getGrade()));
        }

        if (request.getSection() != null) {query.addCriteria(Criteria.where("section").is(request.getSection()));
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(request.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, request.getSortBy()));

        long total = mongoTemplate.count(query, Student.class);

        query.with(pageable);

        List<Student> students = mongoTemplate.find(query, Student.class);

        return new PageImpl<>(students, pageable, total);
    }
}