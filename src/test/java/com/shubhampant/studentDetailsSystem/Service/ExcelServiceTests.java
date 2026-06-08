package com.shubhampant.studentDetailsSystem.Service;


import com.shubhampant.studentDetailsSystem.dto.ExcelUploadResult;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import com.shubhampant.studentDetailsSystem.exceptions.ExcelProcessingException;
import com.shubhampant.studentDetailsSystem.repository.StudentRepository;
import com.shubhampant.studentDetailsSystem.service.ExcelService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExcelServiceTests {

    private Validator validator;
    private ExcelService excelService;

    @BeforeEach
    void setUp() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();

        excelService = new ExcelService(validator, studentRepository);
    }

    @Mock
    private StudentRepository studentRepository;

    private MultipartFile createValidExcelFile() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row header = sheet.createRow(0);
        Row row = sheet.createRow(1);

        row.createCell(0).setCellValue("Shubham");
        row.createCell(1).setCellValue("2007-07-11");
        row.createCell(2).setCellValue(10);
        row.createCell(3).setCellValue("A");
        row.createCell(4).setCellValue("test@test.com");
        row.createCell(5).setCellValue("1234567890");
        row.createCell(6).setCellValue("Delhi");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new MockMultipartFile(
                "file",
                "students.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                out.toByteArray()
        );
    }

    private MultipartFile createInvalidExcelFile() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        sheet.createRow(0);

        Row row = sheet.createRow(1);

        row.createCell(0).setCellValue(""); // invalid name
        row.createCell(1).setCellValue("2007-07-11");
        row.createCell(2).setCellValue(10);
        row.createCell(3).setCellValue("A");
        row.createCell(4).setCellValue("test@test.com");
        row.createCell(5).setCellValue("1234567890");
        row.createCell(6).setCellValue("Delhi");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new MockMultipartFile(
                "file",
                "students.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                out.toByteArray()
        );
    }

    @Test
    void testExcelToStudentsSuccess() throws Exception {

        MultipartFile file = createValidExcelFile();

        when(studentRepository.existsByEmail(anyString())).thenReturn(false);

        ExcelUploadResult result = excelService.excelToStudents(file);

        assertEquals(1, result.getValidStudents().size());
        assertEquals(0, result.getErrors().size());
        verify(studentRepository).existsByEmail("test@test.com");
    }

    @Test
    void testExcelToStudentsValidationError() throws Exception {

        MultipartFile file = createInvalidExcelFile();

        ExcelUploadResult result = excelService.excelToStudents(file);

        assertEquals(0, result.getValidStudents().size());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    void testExcelToStudentsDuplicateEmail() throws Exception {

        MultipartFile file = createValidExcelFile();

        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        ExcelUploadResult result = excelService.excelToStudents(file);

        assertEquals(0, result.getValidStudents().size());
        assertEquals(1, result.getErrors().size());

        assertEquals("Email already exists", result.getErrors().get(0).getErrorMessage());

        verify(studentRepository).existsByEmail("test@test.com");
    }

    @Test
    void testExcelToStudentsInvalidSection() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(1);

        row.createCell(0).setCellValue("Shubham");
        row.createCell(1).setCellValue("2000-07-11");
        row.createCell(2).setCellValue(10);
        row.createCell(3).setCellValue("INVALID");
        row.createCell(4).setCellValue("test@test.com");
        row.createCell(5).setCellValue("1234567890");
        row.createCell(6).setCellValue("Delhi");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        MultipartFile file = new MockMultipartFile("file", out.toByteArray());

        ExcelUploadResult result = excelService.excelToStudents(file);

        assertEquals(0, result.getValidStudents().size());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    void testExcelToStudentsInvalidFile() {

        MultipartFile file = new MockMultipartFile("file", "bad.txt", "text/plain", "hello".getBytes());

        assertThrows(ExcelProcessingException.class, () -> excelService.excelToStudents(file));
    }

    @Test
    void testStudentsToExcel() throws Exception {

        Student student = Student.builder()
                .name("Shubham")
                .dob(LocalDate.of(2000, 7, 11))
                .grade(10)
                .section(Section.A)
                .email("test@test.com")
                .phoneNumber("1234567890")
                .address("Delhi")
                .build();

        ByteArrayInputStream result =
                excelService.studentsToExcel(List.of(student));

        Workbook workbook =
                new XSSFWorkbook(result);

        Sheet sheet = workbook.getSheetAt(0);

        assertEquals("Shubham",
                sheet.getRow(1).getCell(0).getStringCellValue());

        workbook.close();
    }
}
