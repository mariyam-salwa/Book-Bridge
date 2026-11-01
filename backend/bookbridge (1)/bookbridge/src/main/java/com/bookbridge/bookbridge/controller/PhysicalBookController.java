package com.bookbridge.bookbridge.controller;

import com.bookbridge.bookbridge.PhysicalBook;
import com.bookbridge.bookbridge.repository.PhysicalBookRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class PhysicalBookController {

    private final PhysicalBookRepository physicalBookRepository;

    public PhysicalBookController(PhysicalBookRepository physicalBookRepository) {
        this.physicalBookRepository = physicalBookRepository;
    }

    @GetMapping
    public List<PhysicalBook> getAllBooks() {
        return physicalBookRepository.findAll();
    }

    @PostMapping
    public PhysicalBook addBook(@RequestBody PhysicalBook book) {
        if (book.getStatus() == null || book.getStatus().isBlank()) {
            book.setStatus("AVAILABLE");
        }
        return physicalBookRepository.save(book);
    }

    @GetMapping("/{id}")
    public PhysicalBook getBookById(@PathVariable Long id) {
        return physicalBookRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        physicalBookRepository.deleteById(id);
    }
}