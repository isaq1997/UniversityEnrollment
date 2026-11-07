package com.example.demo.service;

import com.example.demo.dto.StudentEmailUpdateRequest;
import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private  StudentRepository studentRepository;

    @Transactional
    public StudentResponse create(StudentRequest req) {
        String email = req.email().trim().toLowerCase();   // same normalization as the entity

        if (studentRepository.existsByEmail(email)) {
            throw new BusinessException("Email already in use");
        }
        if (studentRepository.existsByStudentNumber(req.studentNumber())) {
            throw new BusinessException("Student number already exists");
        }

        Student student = Student.builder()
                .studentNumber(req.studentNumber())
                .name(req.name())
                .email(email)
                .build();

        return toResponse(studentRepository.save(student));
    }

    @Transactional()
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional()
    public StudentResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public StudentResponse updateEmail(Long id, StudentEmailUpdateRequest req) {
        Student student = find(id);
        String email = req.email().trim().toLowerCase();

        if (!student.getEmail().equals(email) && studentRepository.existsByEmail(email)) {
            throw new BusinessException("Email already in use");
        }

        student.changeEmail(email);       // dirty checking saves it, no save() needed
        return toResponse(student);
    }

    private Student find(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }

    private StudentResponse toResponse(Student s) {
        return new StudentResponse(s.getId(), s.getStudentNumber(), s.getName(), s.getEmail());
    }
}