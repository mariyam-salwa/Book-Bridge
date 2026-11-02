package com.bookbridge.bookbridge.controller;

import com.bookbridge.bookbridge.repository.StudentRepository;
import com.bookbridge.bookbridge.repository.MaterialRepository;
import com.bookbridge.bookbridge.repository.PhysicalBookRepository;
import com.bookbridge.bookbridge.repository.BookRequestRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final StudentRepository studentRepository;
    private final MaterialRepository materialRepository;
    private final PhysicalBookRepository bookRepository;
    private final BookRequestRepository requestRepository;

    public AdminController(
            StudentRepository studentRepository,
            MaterialRepository materialRepository,
            PhysicalBookRepository bookRepository,
            BookRequestRepository requestRepository) {

        this.studentRepository = studentRepository;
        this.materialRepository = materialRepository;
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("message", "Admin access successful");
    }

    @GetMapping("/students")
    public List<Map<String, Object>> getStudents() {
        return studentRepository.findAll().stream()
            .map(student -> Map.<String, Object>of(
                "id", student.getId(),
                "name", student.getName(),
                "email", student.getEmail(),
                "role", student.getRole()
            ))
            .toList();
    }

    @DeleteMapping("/students/{id}")
    public Map<String, String> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return Map.of("message", "Student deleted");
    }

    @GetMapping("/materials")
    public Object getMaterials() {
        return materialRepository.findAll();
    }

    @DeleteMapping("/materials/{id}")
    public Map<String, String> deleteMaterial(@PathVariable Long id) {
        materialRepository.deleteById(id);
        return Map.of("message", "Material deleted");
    }

    @GetMapping("/books")
    public Object getBooks() {
        return bookRepository.findAll();
    }

    @DeleteMapping("/books/{id}")
    public Map<String, String> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return Map.of("message", "Book deleted");
    }

    @GetMapping("/book-requests")
    public Object getBookRequests() {
        return requestRepository.findAll();
    }

    @DeleteMapping("/book-requests/{id}")
    public Map<String, String> deleteRequest(@PathVariable Long id) {
        requestRepository.deleteById(id);
        return Map.of("message", "Book request deleted");
    }
}