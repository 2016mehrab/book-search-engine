package com.samurai74.booksearchengine.repository;

import com.samurai74.booksearchengine.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepo extends JpaRepository<Book, Long> {
    @Query(
            value = "select b.* , ts_rank(b.search_vector, to_tsquery('english' , :searchTerm)) as rank from books b where b.search_vector @@ to_tsquery('english',:searchTerm) order by rank desc",
            countQuery = "select count(*) from books b where b.search_vector @@ to_tsquery('english', :searchTerm)",
            nativeQuery = true
    )
    Page<Book> searchBooks(@Param("searchTerm") String searchTerm, Pageable pageable);
}
