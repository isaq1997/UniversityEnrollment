package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(req));
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PatchMapping("/{id}/email")
    public StudentResponse updateEmail(@PathVariable Long id,
                                       @Valid @RequestBody StudentEmailUpdateRequest req) {
        return studentService.updateEmail(id, req);
    }
}