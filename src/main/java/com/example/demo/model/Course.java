package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;
    @Setter
    @Column(nullable = false)
    private String title;
    @Setter
    private int credits;
    private int capacity;
    private int enrolledCount;

    @Builder
    public Course( String code, String title, int credits, int capacity) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.capacity = capacity;
        this.enrolledCount = 0;
    }
    public void incrementEnrolled() {
        if (enrolledCount >= capacity) {
            throw new IllegalStateException("Course is full");
        }
        enrolledCount++;
    }

    public void decrementEnrolled() {
        if (enrolledCount > 0) {
            enrolledCount--;
        }
    }

    public void changeCapacity(int newCapacity) {
        if (newCapacity < enrolledCount) {
            throw new IllegalArgumentException("Capacity cannot be below current enrollment");
        }
        this.capacity = newCapacity;
    }
}
