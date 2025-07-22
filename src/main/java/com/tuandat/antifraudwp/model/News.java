package com.tuandat.antifraudwp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageUrl;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status; // active, hidden

    // getters/setters
} 