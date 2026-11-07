package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank String code,
        @NotBlank String title,
        @Min(1) int credits,
        @Min(1) int capacity) {}
