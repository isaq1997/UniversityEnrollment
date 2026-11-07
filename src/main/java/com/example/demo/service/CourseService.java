package com.example.demo.service;

import com.example.demo.dto.CourseRequest;
import com.example.demo.dto.CourseResponse;
import com.example.demo.dto.CourseUpdateRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private  CourseRepository courseRepository;

    @Transactional
    public CourseResponse create(CourseRequest req) {
        if (courseRepository.existsByCode(req.code())) {
            throw new BusinessException("Course code already exists");
        }

        Course course = Course.builder()
                .code(req.code())
                .title(req.title())
                .credits(req.credits())
                .capacity(req.capacity())
                .build();

        return toResponse(courseRepository.save(course));
    }

    @Transactional()
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional()
    public CourseResponse getById(Long id) {
        return toResponse(courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id)));
    }

    @Transactional
    public CourseResponse update(Long id, CourseUpdateRequest req) {
        // lock: capacity change must not race with enrollments
        Course course = courseRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));

        course.setTitle(req.title());
        course.setCredits(req.credits());
        course.changeCapacity(req.capacity());   // validates against enrolledCount

        return toResponse(course);
    }

    private CourseResponse toResponse(Course c) {
        return new CourseResponse(c.getId(), c.getCode(), c.getTitle(),
                c.getCredits(), c.getCapacity(), c.getEnrolledCount());
    }
}
