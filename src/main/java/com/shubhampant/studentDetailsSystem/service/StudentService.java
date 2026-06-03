package com.shubhampant.studentDetailsSystem.service;

import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import com.shubhampant.studentDetailsSystem.exceptions.StudentNotFoundException;
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

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

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

    public Student getStudentById(String id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found with id: " +  id));
    }

    public void deleteStudent(String id) {
        log.warn(
                "Deleting student with id: {}",
                id
        );
        Student student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        repository.deleteById(id);

        log.info("Successfully deleted student with id: {}", id);
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
                repository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

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

        log.info(
                "Filtering students with name={}, grade={}, section={}",
                request.getName(),
                request.getGrade(),
                request.getSection()
        );

        return repository.filterStudents(request);
    }

    public List<Student> saveAllStudents(List<Student> students) {
        log.info(
                "Saving {} students from Excel upload",
                students.size()
        );
        log.info("Successfully saved all students");
        return repository.saveAll(students);
    }


    public List<Student> getAllStudentsForExport() {
        log.info("Exporting all students to Excel");
        return repository.findAll();
    }
}
