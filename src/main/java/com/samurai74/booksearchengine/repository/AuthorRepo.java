package com.samurai74.booksearchengine.repository;

import com.samurai74.booksearchengine.domain.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {

    Optional<Author > findByNameIgnoreCase(String name);

}
