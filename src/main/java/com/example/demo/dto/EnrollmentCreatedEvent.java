package com.example.demo.dto;

public record EnrollmentCreatedEvent(
        String studentEmail,
        String studentName,
        String courseCode,
        String courseTitle) {}
