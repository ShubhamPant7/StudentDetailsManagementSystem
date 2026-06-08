package com.shubhampant.studentDetailsSystem.Controller;

import com.shubhampant.studentDetailsSystem.controller.StudentController;
import com.shubhampant.studentDetailsSystem.dto.ExcelUploadResult;
import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.service.ExcelService;
import com.shubhampant.studentDetailsSystem.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTests {

    @Mock
    private StudentService studentService;

    @Mock
    private ExcelService excelService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testGetStudentById() {
        Student student = Student.builder().id("1").name("Shubham").build();

        when(studentService.getStudentById("1")).thenReturn(student);

        Student result = studentController.getStudentById("1");
        assertEquals("Shubham", result.getName());
        verify(studentService).getStudentById("1");
    }

    @Test
    void testCreateStudent() {

        Student student = Student.builder()
                .name("Shubham")
                .build();

        when(studentService.createStudent(student)).thenReturn(student);

        Student result = studentController.createStudent(student);

        assertEquals("Shubham", result.getName());

        verify(studentService).createStudent(student);
    }

    @Test
    void testDeleteStudent() {

        studentController.deleteStudent("1");

        verify(studentService).deleteStudent("1");
    }

    @Test
    void testUpdateStudent() {

        Student student = Student.builder()
                .name("Updated")
                .build();

        when(studentService.updateStudent("1", student)).thenReturn(student);

        Student result = studentController.updateStudent(student, "1");

        assertEquals("Updated", result.getName());

        verify(studentService).updateStudent("1", student);
    }

    @Test
    void testUploadExcel() {

        MultipartFile file =
                new MockMultipartFile(
                        "file",
                        new byte[0]
                );

        ExcelUploadResult result =
                new ExcelUploadResult(
                        List.of(),
                        List.of()
                );

        when(excelService.excelToStudents(file)).thenReturn(result);

        ExcelUploadResult response = studentController.uploadExcel(file);

        assertEquals(result, response);

        verify(excelService).excelToStudents(file);
        verify(studentService).saveAllStudents(List.of());
    }

    @Test
    void testGetStudents() {

        StudentFilterRequest request = new StudentFilterRequest();

        request.setPage(0);
        request.setSize(150); // over limit

        Page<Student> page = new PageImpl<>(List.of());

        when(studentService.filterStudents(request)).thenReturn(page);

        Page<Student> result = studentController.getStudents(request);

        assertEquals(page, result);

        assertEquals(100, request.getSize());

        verify(studentService).filterStudents(request);
    }

    @Test
    void testExportStudents() {

        List<Student> students = List.of(
                Student.builder()
                        .name("Shubham")
                        .build()
        );

        ByteArrayInputStream excelFile = new ByteArrayInputStream(new byte[]{1, 2, 3});

        when(studentService.getAllStudentsForExport()).thenReturn(students);

        when(excelService.studentsToExcel(students)).thenReturn(excelFile);

        ResponseEntity<InputStreamResource> response = studentController.exportStudents();

        assertEquals(200, response.getStatusCode().value());

        verify(studentService).getAllStudentsForExport();

        verify(excelService).studentsToExcel(students);
    }
}
