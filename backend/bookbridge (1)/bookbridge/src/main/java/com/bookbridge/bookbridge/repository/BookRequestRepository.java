package com.bookbridge.bookbridge.repository;

import com.bookbridge.bookbridge.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    List<BookRequest> findByBuyerEmail(String buyerEmail);

    List<BookRequest> findByBookId(Long bookId);

}