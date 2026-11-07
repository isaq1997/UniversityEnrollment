package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody EnrollRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> drop(@PathVariable Long id) {
        enrollmentService.drop(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentResponse> byStudent(@PathVariable Long studentId) {
        return enrollmentService.getByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<EnrollmentResponse> byCourse(@PathVariable Long courseId) {
        return enrollmentService.getByCourse(courseId);
    }
}