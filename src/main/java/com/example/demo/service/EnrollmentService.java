package com.example.demo.service;

import com.example.demo.dto.EnrollRequest;
import com.example.demo.dto.EnrollmentCreatedEvent;
import com.example.demo.dto.EnrollmentResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;
import com.example.demo.model.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private  StudentRepository studentRepository;
    @Autowired
    private  CourseRepository courseRepository;
    @Autowired
    private  EnrollmentRepository enrollmentRepository;
    @Autowired

    private  ApplicationEventPublisher eventPublisher;

    @Transactional
    public EnrollmentResponse enroll(EnrollRequest req) {
        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + req.studentId()));

        // row lock: held until this method returns
        Course course = courseRepository.findByIdForUpdate(req.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + req.courseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new BusinessException("Student already enrolled in this course");
        }

        course.incrementEnrolled();   // throws IllegalStateException("Course is full")

        Enrollment enrollment = enrollmentRepository.save(new Enrollment(student, course));
        /*eventPublisher.publishEvent(new EnrollmentCreatedEvent(
                student.getEmail(), student.getName(),
                course.getCode(), course.getTitle()));*/
        return toResponse(enrollment);
    }

    @Transactional
    public void drop(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + enrollmentId));

        // lock the course before changing its count
        Course course = courseRepository.findByIdForUpdate(enrollment.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.decrementEnrolled();
        enrollmentRepository.delete(enrollment);
    }

    @Transactional()
    public List<EnrollmentResponse> getByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::toResponse).toList();
    }

    @Transactional()
    public List<EnrollmentResponse> getByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::toResponse).toList();
    }

    private EnrollmentResponse toResponse(Enrollment e) {
        return new EnrollmentResponse(
                e.getId(),
                e.getStudent().getId(), e.getStudent().getName(),
                e.getCourse().getId(), e.getCourse().getCode(),
                e.getEnrolledAt());
    }
}