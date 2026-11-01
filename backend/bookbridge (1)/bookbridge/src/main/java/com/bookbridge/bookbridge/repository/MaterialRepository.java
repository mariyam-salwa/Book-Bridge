package com.bookbridge.bookbridge.repository;

import com.bookbridge.bookbridge.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

}