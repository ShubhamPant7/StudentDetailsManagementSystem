package com.shubhampant.studentDetailsSystem.service;

import com.shubhampant.studentDetailsSystem.dto.ExcelUploadResult;
import com.shubhampant.studentDetailsSystem.dto.RowError;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.enums.Section;
import com.shubhampant.studentDetailsSystem.exceptions.ExcelProcessingException;
import com.shubhampant.studentDetailsSystem.repository.StudentRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class ExcelService {

    private final Validator validator;
    private final StudentRepository studentRepository;

    public ExcelService(Validator validator, StudentRepository studentRepository) {
        this.validator = validator;
        this.studentRepository = studentRepository;
    }

    public ExcelUploadResult excelToStudents(MultipartFile file) {
        List<Student> students = new ArrayList<>();
        List<RowError> errors = new ArrayList<>();

        log.info("Started Excel upload");

        try {
            InputStream inputStream = file.getInputStream();

            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                try {

                    Row row = sheet.getRow(i);

                    Student student = Student.builder()
                            .name(row.getCell(0).getStringCellValue())
                            .grade((int) row.getCell(2).getNumericCellValue())
                            .section(Section.valueOf(row.getCell(3).getStringCellValue()))
                            .email(row.getCell(4).getStringCellValue())
                            .build();

                    Cell dobCell = row.getCell(1);

                    if (dobCell.getCellType() == CellType.NUMERIC) {
                        student.setDob(
                                dobCell.getLocalDateTimeCellValue().toLocalDate()
                        );
                    } else {
                        student.setDob(
                                LocalDate.parse(
                                        dobCell.getStringCellValue()
                                )
                        );
                    }

                    student.setPhoneNumber(
                            row.getCell(5).getStringCellValue()
                    );

                    student.setAddress(
                            row.getCell(6).getStringCellValue()
                    );

                    Set<ConstraintViolation<Student>> violations =
                            validator.validate(student);

                    if (!violations.isEmpty()) {

                        StringBuilder errorMessage = new StringBuilder();

                        for (ConstraintViolation<Student> violation : violations) {

                            errorMessage
                                    .append(violation.getMessage())
                                    .append(", ");
                        }

                        errors.add(
                                new RowError(
                                        i + 1,
                                        errorMessage.toString()
                                )
                        );

                        continue;
                    }

                    if (studentRepository.existsByEmail(student.getEmail())) {

                        errors.add(
                                new RowError(
                                        i + 1,
                                        "Email already exists"
                                )
                        );

                        continue;
                    }

                    students.add(student);

                } catch (Exception e) {

                    errors.add(
                            new RowError(
                                    i + 1,
                                    e.getMessage()
                            )
                    );
                }
            }

            workbook.close();
        } catch (Exception e) {

            log.error(
                    "Failed to parse Excel file",
                    e
            );

            throw new ExcelProcessingException(
                    "Failed to parse Excel file"
            );
        }

        log.info(
                "Successfully parsed {} students from Excel",
                students.size()
        );

        return new ExcelUploadResult(
                students,
                errors
        );
    }

    public ByteArrayInputStream studentsToExcel(List<Student> students) {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Students");

            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("DOB");
            headerRow.createCell(2).setCellValue("Grade");
            headerRow.createCell(3).setCellValue("Section");
            headerRow.createCell(4).setCellValue("Email");
            headerRow.createCell(5).setCellValue("Phone Number");
            headerRow.createCell(6).setCellValue("Address");

            int rowNum = 1;

            for (Student student : students) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(student.getName());

                row.createCell(1).setCellValue(student.getDob().toString());

                row.createCell(2).setCellValue(student.getGrade());

                row.createCell(3).setCellValue(student.getSection().name());

                row.createCell(4).setCellValue(student.getEmail());

                row.createCell(5).setCellValue(student.getPhoneNumber());

                row.createCell(6).setCellValue(student.getAddress());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);

            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {

            throw new ExcelProcessingException("Failed to export Excel file");
        }
    }
}
