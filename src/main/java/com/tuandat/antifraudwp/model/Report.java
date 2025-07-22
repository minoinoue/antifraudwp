package com.tuandat.antifraudwp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reporterName;
    private String reporterEmail;
    private String phone;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String evidenceUrl;
    private LocalDateTime createdAt;
    private String status; // pending, processing, done
    private String adminNote;

    // getters/setters
} 