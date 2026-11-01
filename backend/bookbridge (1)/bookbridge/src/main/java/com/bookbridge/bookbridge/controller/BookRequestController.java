package com.bookbridge.bookbridge.controller;

import com.bookbridge.bookbridge.BookRequest;
import com.bookbridge.bookbridge.PhysicalBook;
import com.bookbridge.bookbridge.repository.BookRequestRepository;
import com.bookbridge.bookbridge.repository.PhysicalBookRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/book-requests")
@CrossOrigin(origins = "*")
public class BookRequestController {

    private final BookRequestRepository bookRequestRepository;
    private final PhysicalBookRepository physicalBookRepository;

    public BookRequestController(
            BookRequestRepository bookRequestRepository,
            PhysicalBookRepository physicalBookRepository) {

        this.bookRequestRepository = bookRequestRepository;
        this.physicalBookRepository = physicalBookRepository;
    }

    // 1. Create a new book request
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody BookRequest request) {

        if (request.getBookId() == null ||
            request.getBuyerName() == null ||
            request.getBuyerEmail() == null ||
            request.getBuyerName().isBlank() ||
            request.getBuyerEmail().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Book ID, buyer name and email are required.");
        }

        PhysicalBook book = physicalBookRepository
                .findById(request.getBookId())
                .orElse(null);

        if (book == null) {
            return ResponseEntity.badRequest()
                    .body("Book not found.");
        }

        if (!"AVAILABLE".equalsIgnoreCase(book.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("This book is not available.");
        }

        request.setStatus("PENDING");

        BookRequest savedRequest =
                bookRequestRepository.save(request);

        return ResponseEntity.ok(savedRequest);
    }

    // 2. View all requests
    @GetMapping
    public List<BookRequest> getAllRequests() {
        return bookRequestRepository.findAll();
    }

    // 3. View requests made by a student
    @GetMapping("/buyer/{email}")
    public List<BookRequest> getRequestsByBuyer(
            @PathVariable String email) {

        return bookRequestRepository.findByBuyerEmail(email);
    }

    // 4. View requests for a particular book
    @GetMapping("/book/{bookId}")
    public List<BookRequest> getRequestsByBook(
            @PathVariable Long bookId) {

        return bookRequestRepository.findByBookId(bookId);
    }

    // 5. Update request status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        BookRequest request = bookRequestRepository
                .findById(id)
                .orElse(null);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        String status = body.get("status");

        if (status == null ||
            !List.of("ACCEPTED", "REJECTED", "COMPLETED")
                    .contains(status.toUpperCase())) {

            return ResponseEntity.badRequest()
                    .body("Invalid request status.");
        }

        request.setStatus(status.toUpperCase());

        return ResponseEntity.ok(
                bookRequestRepository.save(request));
    }
}