package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollRequest(@NotNull Long studentId, @NotNull Long courseId) {}

