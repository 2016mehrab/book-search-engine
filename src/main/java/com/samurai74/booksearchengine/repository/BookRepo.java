package com.samurai74.booksearchengine.repository;

import com.samurai74.booksearchengine.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Long> {
    @Query(
            value = "select b.* , ts_rank(b.search_vector, to_tsquery('english' , :searchTerm)) as rank from books b where b.search_vector @@ to_tsquery('english',:searchTerm) order by rank desc",
            countQuery = "select count(*) from books b where b.search_vector @@ to_tsquery('english', :searchTerm)",
            nativeQuery = true
    )
    Page<Book> searchBooks(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(nativeQuery = true,
    value = "select b.* from books b inner join books_authors ba on b.book_id = ba.book_id inner join authors a on ba.author_id = a.author_id where ba.author_name=:authorName ",
            countQuery ="select count(*) from books b inner join books_authors ba on b.book_id = ba.book_id inner join authors a on ba.author_id = a.author_id where a.author_id=:authorId "
    )
    Page<Book> findBooksByAuthorId(@Param("authorId") long id, Pageable pageable);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    void deleteByIsbn(String isbn);
}
