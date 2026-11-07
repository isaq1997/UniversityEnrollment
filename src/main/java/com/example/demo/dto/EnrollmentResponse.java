package com.example.demo.dto;

import java.time.LocalDateTime;

public record EnrollmentResponse(Long id, Long studentId, String studentName,
                                 Long courseId, String courseCode, LocalDateTime enrolledAt) {}