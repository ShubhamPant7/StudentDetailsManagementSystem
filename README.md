# Student Details System

A Spring Boot backend application for managing student records with support for:

* CRUD operations
* Pagination and sorting
* Filtering students
* Excel upload and export
* Validation and exception handling
* MongoDB integration
* REST APIs

---

# Tech Stack

## Backend

* Java
* Spring Boot
* Spring Security
* MongoDB
* Spring Data MongoDB
* Maven
* Apache POI
* Lombok
* SLF4J Logging
* Spring Validation

## Tools

* Postman
* IntelliJ IDEA
* Git & GitHub

---

# Features Implemented

## Student Management

* Create student
* Get all students
* Get student by ID
* Update student
* Delete student

## Pagination & Sorting

Supports:

* Page number
* Page size
* Sorting by fields
* Ascending/descending ordering

Example:

```http
GET /students?page=0&size=5&sortBy=name&direction=asc
```

---

## Filtering

Students can be filtered by:

* Name
* Grade
* Section

Example:

```http
GET /students?name=shubham&grade=12&section=A
```

---

## Excel Upload

Supports bulk student upload through Excel files.

Implemented using:

* MultipartFile
* Apache POI
* Validation handling

Endpoint:

```http
POST /students/upload
```

---

## Excel Export

Exports all student records into an Excel file.

Endpoint:

```http
GET /students/export
```

---

## Validation

Implemented validation using Jakarta Validation annotations.

Examples:

* `@NotBlank`
* `@Email`
* `@Min`
* `@Max`

---
## Logging

Implemented structured logging using:

* SLF4J
* Lombok `@Slf4j`

Examples:

* Student creation logs
* Update logs
* Delete logs
* Excel upload logs
* Error logs

---

# API Endpoints

| Method | Endpoint           | Description              |
| ------ | ------------------ | ------------------------ |
| POST   | `/students`        | Create student           |
| GET    | `/students`        | Get/filter students      |
| GET    | `/students/{id}`   | Get student by ID        |
| PUT    | `/students/{id}`   | Update student           |
| DELETE | `/students/{id}`   | Delete student           |
| POST   | `/students/upload` | Upload Excel file        |
| GET    | `/students/export` | Export students to Excel |

---

# Project Structure

```text
src/main/java
│
├── controller
├── service
├── repository
├── entity
├── dto
├── enums
├── exception
└── config
```

---

# How to Run

## Clone Repository

```bash
git clone <repository-url>
```

---

## Navigate to Project

```bash
cd studentDetailsSystem
```

---

## Run Application

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

# MongoDB Configuration

Configure MongoDB connection in:

```properties
application.properties
```

Example:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/studentdb
```

---


# Plan To Implement / Future Improvements

## Backend

* Detailed Spring Security
* JUnit Testing
* MySQL Integration
* Spring Data JPA
* Spring Data JDBC
* Microservices Basics

## Infrastructure & Messaging

* Redis Caching
* RabbitMQ

## Frontend

* Basic Frontend Starter
* JavaScript Integration
* Angular Frontend


# Notes

This project was built as part of learning backend development concepts using Spring Boot and MongoDB while focusing on clean architecture, REST API design, validation, exception handling, and file processing.
