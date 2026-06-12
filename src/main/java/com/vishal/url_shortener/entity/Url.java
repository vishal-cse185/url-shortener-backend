package com.vishal.url_shortener.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
@Data
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(unique = true)
    private String shortCode;

    @Column(unique = true)
    private String customAlias;

    private String category;

    private String riskLevel;

    private Long clickCount = 0L;

    private LocalDateTime createdAt = LocalDateTime.now();
}