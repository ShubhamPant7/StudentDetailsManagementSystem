package com.shubhampant.studentDetailsSystem.controller;

import com.shubhampant.studentDetailsSystem.dto.StudentFilterRequest;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import com.shubhampant.studentDetailsSystem.service.ExcelService;
import com.shubhampant.studentDetailsSystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final ExcelService excelService;

    public StudentController(StudentService studentService, ExcelService excelService) {
        this.studentService = studentService;
        this.excelService = excelService;
    }
    private static final int maximumEntriesPerPage = 100;

    @PostMapping
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping
    public Page<Student> getStudents(StudentFilterRequest request) {
        request.setSize(
                Math.min(
                        request.getSize(),
                        maximumEntriesPerPage
                )
        );
        return studentService.filterStudents(request);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@ Valid @RequestBody Student student, @PathVariable String id) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
    }

    @PostMapping("/upload")
    public List<Student> uploadExcel(@RequestParam("file") MultipartFile file) {
        List<Student> students = excelService.excelToStudents(file);
        return studentService.saveAllStudents(students);
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportStudents() {
        List<Student> students = studentService.getAllStudentsForExport();

        ByteArrayInputStream excelFile = excelService.studentsToExcel(students);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment; filename=students.xlsx");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(new InputStreamResource(excelFile));
    }
}
