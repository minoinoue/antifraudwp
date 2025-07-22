package com.tuandat.antifraudwp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private String status; // active, hidden

    // getters/setters
} 