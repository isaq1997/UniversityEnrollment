package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String studentNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder
    public Student(String studentNumber, String name, String email) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = normalize(email);
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = normalize(newEmail);
    }

    private static String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
