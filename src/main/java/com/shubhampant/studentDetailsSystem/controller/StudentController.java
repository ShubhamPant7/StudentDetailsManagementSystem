package com.shubhampant.studentDetailsSystem.controller;

import com.shubhampant.studentDetailsSystem.dto.ExcelUploadResult;
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

//REST endpoints for interacting with student records. Provides pagination for large results.
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final ExcelService excelService;

    public StudentController(StudentService studentService, ExcelService excelService) {
        this.studentService = studentService;
        this.excelService = excelService;
    }

    //Caps page size to prevent users from getting overloaded with data.
    private static final int maximumEntriesPerPage = 100;

    @PostMapping
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping
    public Page<Student> getStudents(StudentFilterRequest request) {
        request.setSize(Math.min(request.getSize(), maximumEntriesPerPage));
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
    public ExcelUploadResult uploadExcel(@RequestParam("file") MultipartFile file) {
        ExcelUploadResult result = excelService.excelToStudents(file);
        studentService.saveAllStudents(result.getValidStudents());
        return result;
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportStudents() {

        //Generate excel file in memory and return it as a downloadable attachment.
        List<Student> students = studentService.getAllStudentsForExport();

        ByteArrayInputStream excelFile = excelService.studentsToExcel(students);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment; filename=students.xlsx");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(new InputStreamResource(excelFile));
    }
}
