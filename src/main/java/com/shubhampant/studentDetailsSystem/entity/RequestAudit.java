package com.shubhampant.studentDetailsSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


// Entity representing an API request audit log.
@Entity
@Table(name = "audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String endpoint;

    @Column(name = "http_method")
    private String httpMethod;

    private String status;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
