package com.bookbridge.bookbridge.repository;

import com.bookbridge.bookbridge.PhysicalBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalBookRepository extends JpaRepository<PhysicalBook, Long> {

}