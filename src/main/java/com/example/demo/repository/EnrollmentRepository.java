package com.example.demo.repository;

import com.example.demo.model.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

        boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

        @EntityGraph(attributePaths = {"student", "course"})
        List<Enrollment> findByStudentId(Long studentId);

        @EntityGraph(attributePaths = {"student", "course"})
        List<Enrollment> findByCourseId(Long courseId);
    }

