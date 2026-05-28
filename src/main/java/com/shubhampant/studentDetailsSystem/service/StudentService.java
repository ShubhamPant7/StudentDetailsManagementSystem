package com.shubhampant.studentDetailsSystem.service;

import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import com.shubhampant.studentDetailsSystem.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {

    @Autowired
    private StudentRepository repository;


    public Student createStudent(Student student) {
        log.info(
                "Creating student with email: {}",
                student.getEmail()
        );
        return repository.save(student);
    }

    public Page<Student> getStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Optional<Student> getStudentById(String Id) {
        return repository.findById(Id);
    }

    public void deleteStudent(String id) {
        log.warn(
                "Deleting student with id: {}",
                id
        );
        repository.deleteById(id);
    }

    public Student updateStudent(
            String id,
            Student updatedStudent
    ) {

        log.info(
                "Updating student with id: {}",
                id
        );

        Student current =
                repository.findById(id).orElse(null);

        if (current == null) {

            log.error(
                    "Student not found with id: {}",
                    id
            );

            return null;
        }

        current.setName(updatedStudent.getName());
        current.setDob(updatedStudent.getDob());
        current.setAddress(updatedStudent.getAddress());
        current.setEmail(updatedStudent.getEmail());
        current.setPhoneNumber(updatedStudent.getPhoneNumber());
        current.setGrade(updatedStudent.getGrade());
        current.setSection(updatedStudent.getSection());

        log.info(
                "Successfully updated student with id: {}",
                id
        );

        return repository.save(current);

    }

    public Page<Student> filterStudents(StudentFilterRequest request) {

        Sort sort;

        if (request.getDirection().equalsIgnoreCase("desc")) {
            sort = Sort.by(request.getSortBy()).descending();
        } else {
            sort = Sort.by(request.getSortBy()).ascending();
        }

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getMaximumPageSize(),
                sort
        );

        String name = request.getName();
        Integer grade = request.getGrade();
        Section section = request.getSection();

        if (name != null && grade != null && section != null) {
            return repository
                    .findByNameContainingIgnoreCaseAndGradeAndSection(
                            name,
                            grade,
                            section,
                            pageable
                    );
        }

        if (name != null && grade != null) {
            return repository
                    .findByNameContainingIgnoreCaseAndGrade(
                            name,
                            grade,
                            pageable
                    );
        }

        if (name != null && section != null) {
            return repository
                    .findByNameContainingIgnoreCaseAndSection(
                            name,
                            section,
                            pageable
                    );
        }

        if (grade != null && section != null) {
            return repository
                    .findByGradeAndSection(
                            grade,
                            section,
                            pageable
                    );
        }

        if (name != null) {
            return repository
                    .findByNameContainingIgnoreCase(
                            name,
                            pageable
                    );
        }

        if (grade != null) {
            return repository.findByGrade(
                    grade,
                    pageable
            );
        }

        if (section != null) {
            return repository.findBySection(
                    section,
                    pageable
            );
        }

        return repository.findAll(pageable);
    }

    public List<Student> saveAllStudents(List<Student> students) {
        return repository.saveAll(students);
    }

    public List<Student> getAllStudentsForExport() {
        return repository.findAll();
    }
}
