package com.bookbridge.bookbridge.controller;

import com.bookbridge.bookbridge.Student;
import com.bookbridge.bookbridge.repository.StudentRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        var spec = new javax.crypto.spec.PBEKeySpec(
            password.toCharArray(), salt, 120000, 256
        );

        byte[] hash = javax.crypto.SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(salt)
            + ":" + Base64.getEncoder().encodeToString(hash);
    }

    private boolean verifyPassword(String password, String stored)
            throws Exception {
        String[] parts = stored.split(":");

        byte[] salt = Base64.getDecoder().decode(parts[0]);

        var spec = new javax.crypto.spec.PBEKeySpec(
            password.toCharArray(), salt, 120000, 256
        );

        byte[] hash = javax.crypto.SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec).getEncoded();

        byte[] original = Base64.getDecoder().decode(parts[1]);

        return MessageDigest.isEqual(hash, original);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        try {
            if (student.getName() == null || student.getName().isBlank()
                    || student.getEmail() == null
                    || student.getEmail().isBlank()
                    || student.getPasswordHash() == null
                    || student.getPasswordHash().isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "All fields are required"));
            }

            if (studentRepository.existsByEmail(student.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already registered"));
            }

            String password = student.getPasswordHash();
            student.setPasswordHash(hashPassword(password));
            student.setRole("STUDENT");

            Student saved = studentRepository.save(student);

            return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "id", saved.getId(),
                "name", saved.getName(),
                "email", saved.getEmail(),
                "role", saved.getRole()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Registration failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student login) {
        try {
            if (login.getEmail() == null || login.getPasswordHash() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password are required"));
            }

            Student student = studentRepository
                .findByEmail(login.getEmail()).orElse(null);

            if (student == null || !verifyPassword(
                    login.getPasswordHash(), student.getPasswordHash())) {
                return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid email or password"));
            }

            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "id", student.getId(),
                "name", student.getName(),
                "email", student.getEmail(),
                "role", student.getRole()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Login failed"));
        }
    }
}
