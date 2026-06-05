package com.shubhampant.studentDetailsSystem.Service;

import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import com.shubhampant.studentDetailsSystem.exceptions.StudentNotFoundException;
import com.shubhampant.studentDetailsSystem.repository.StudentRepository;
import com.shubhampant.studentDetailsSystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setup() {
        student = Student.builder()
                .id("1")
                .name("Shubham")
                .dob(LocalDate.of(2000, 7, 11))
                .grade(10)
                .email("test@test.com")
                .section(Section.A)
                .phoneNumber("1234567890")
                .address("Delhi")
                .build();
    }

    @Test
    public void testGetStudentById() {

        when(studentRepository.findById("1")).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById("1");

        verify(studentRepository).findById("1");

        assertEquals("Shubham", result.getName());
        assertEquals(LocalDate.of(2000, 7, 11), result.getDob());
        assertEquals(10, result.getGrade());
        assertEquals("test@test.com", result.getEmail());
        assertEquals(Section.A, result.getSection());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("Delhi", result.getAddress());
    }

    @Test
    void testGetStudentByIdNotFound() {

        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById("1")
        );

        verify(studentRepository).findById("1");
    }

    @Test
    public void testCreateStudent() {
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.createStudent(student);

        verify(studentRepository).save(student);

        assertEquals("Shubham", result.getName());
        assertEquals(LocalDate.of(2000, 7, 11), result.getDob());
        assertEquals(10, result.getGrade());
        assertEquals("test@test.com", result.getEmail());
        assertEquals(Section.A, result.getSection());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("Delhi", result.getAddress());
    }

    @Test
    public void testCreateStudentWhenRepositoryFails() {

        when(studentRepository.save(student))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.createStudent(student)
        );

        assertEquals("Database error", exception.getMessage());

        verify(studentRepository).save(student);
    }

    @Test
    void testGetStudents() {

        Page<Student> studentPage =
                new PageImpl<>(List.of(student));

        when(studentRepository.findAll(any(Pageable.class)))
                .thenReturn(studentPage);

        Page<Student> result = studentService.getStudents(0, 10);

        verify(studentRepository).findAll(any(Pageable.class));

        assertEquals(1, result.getContent().size());
        assertEquals("Shubham",
                result.getContent().get(0).getName());
    }

    @Test
    void testGetStudentsCreatesCorrectPageable() {

        Page<Student> studentPage =
                new PageImpl<>(List.of(student));

        when(studentRepository.findAll(any(Pageable.class)))
                .thenReturn(studentPage);

        studentService.getStudents(2, 25);

        verify(studentRepository)
                .findAll(PageRequest.of(2, 25));
    }

    @Test
    void testDeleteStudent() {

        when(studentRepository.findById("1"))
                .thenReturn(Optional.of(student));

        studentService.deleteStudent("1");

        verify(studentRepository).findById("1");
        verify(studentRepository).deleteById("1");
    }

    @Test
    void testDeleteStudentNotFound() {

        when(studentRepository.findById("1"))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception =
                assertThrows(
                        StudentNotFoundException.class,
                        () -> studentService.deleteStudent("1")
                );

        assertEquals(
                "Student not found with id: 1",
                exception.getMessage()
        );

        verify(studentRepository).findById("1");
    }

    @Test
    void testUpdateStudent() {

        Student updatedStudent = Student.builder()
                .name("Rahul")
                .dob(LocalDate.of(2001, 1, 1))
                .grade(11)
                .email("rahul@test.com")
                .section(Section.B)
                .phoneNumber("9999999999")
                .address("Mumbai")
                .build();

        when(studentRepository.findById("1"))
                .thenReturn(Optional.of(student));

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Student result =
                studentService.updateStudent("1", updatedStudent);

        verify(studentRepository).findById("1");
        verify(studentRepository).save(any(Student.class));

        assertEquals("Rahul", result.getName());
        assertEquals(LocalDate.of(2001, 1, 1), result.getDob());
        assertEquals(11, result.getGrade());
        assertEquals("rahul@test.com", result.getEmail());
        assertEquals(Section.B, result.getSection());
        assertEquals("9999999999", result.getPhoneNumber());
        assertEquals("Mumbai", result.getAddress());
    }

    @Test
    void testUpdateStudentNotFound() {

        when(studentRepository.findById("1"))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception =
                assertThrows(
                        StudentNotFoundException.class,
                        () -> studentService.updateStudent("1", student)
                );

        assertEquals(
                "Student not found with id: 1",
                exception.getMessage()
        );

        verify(studentRepository).findById("1");
    }

    @Test
    void testSaveAllStudents() {

        List<Student> students = List.of(student);

        when(studentRepository.saveAll(students))
                .thenReturn(students);

        List<Student> result =
                studentService.saveAllStudents(students);

        verify(studentRepository).saveAll(students);

        assertEquals(1, result.size());
        assertEquals("Shubham", result.get(0).getName());
    }

    @Test
    void testSaveAllStudentsWhenRepositoryFails() {

        List<Student> students = List.of(student);

        when(studentRepository.saveAll(students))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> studentService.saveAllStudents(students)
                );

        assertEquals("Database error", exception.getMessage());

        verify(studentRepository).saveAll(students);
    }

    @Test
    void testGetAllStudentsForExport() {

        List<Student> students = List.of(student);

        when(studentRepository.findAll())
                .thenReturn(students);

        List<Student> result =
                studentService.getAllStudentsForExport();

        verify(studentRepository).findAll();

        assertEquals(1, result.size());
        assertEquals("Shubham", result.get(0).getName());
    }

    @Test
    void testGetAllStudentsForExportWhenRepositoryFails() {

        when(studentRepository.findAll())
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> studentService.getAllStudentsForExport()
                );

        assertEquals("Database error", exception.getMessage());

        verify(studentRepository).findAll();
    }

    @Test
    void testFilterStudents() {

        StudentFilterRequest request = new StudentFilterRequest();
        request.setName("Shubham");
        request.setGrade(10);
        request.setSection(Section.A);

        List<Student> students = List.of(student);

        Page<Student> page =
                new PageImpl<>(students);

        when(studentRepository.filterStudents(request))
                .thenReturn(page);

        Page<Student> result =
                studentService.filterStudents(request);

        verify(studentRepository).filterStudents(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("Shubham", result.getContent().get(0).getName());
    }

    @Test
    void testFilterStudentsWhenRepositoryFails() {

        StudentFilterRequest request = new StudentFilterRequest();
        request.setName("Shubham");

        when(studentRepository.filterStudents(request))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> studentService.filterStudents(request)
                );

        assertEquals("Database error", exception.getMessage());

        verify(studentRepository).filterStudents(request);
    }
}
