package com.samurai74.booksearchengine.repository;

import com.samurai74.booksearchengine.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepo extends JpaRepository<Author, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Author > findByNameIgnoreCase(String name);
}
