package com.example.demo.dto;

public record CourseResponse(Long id, String code, String title,
                             int credits, int capacity, int enrolledCount) {}