package com.samurai74.booksearchengine.repository;

import com.samurai74.booksearchengine.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    @Query(
            value="SELECT " +
                    "b.book_id, " +
                    "ts_headline('english', b.title, plainto_tsquery('english', :searchTerm),'StartSel=<span>,StopSel=</span>' ) AS title, " +
                    "b.rating, " +
                    "ts_headline('english', b.description, plainto_tsquery('english', :searchTerm),'StartSel=<span>,StopSel=</span>' ) AS description, " +
                    "b.language, " +
                    "ts_headline('english', b.isbn, plainto_tsquery('english', :searchTerm),'StartSel=<span>,StopSel=</span>' ) AS isbn, " +
                    "b.book_format , " +
                    "b.edition, " +
                    "b.pages, " +
                    "b.publisher, " +
                    "b.publish_date , " +
                    "b.first_publish_date , " +
                    "b.liked_percent , " +
                    "b.price, " +
                    "ts_rank(b.search_vector, plainto_tsquery('english', :searchTerm)) AS rank " +
                    "FROM books b " +
                    "WHERE b.search_vector @@ plainto_tsquery('english', :searchTerm) " +
                    "ORDER BY rank DESC",
            countQuery = "select count(*) from books b where b.search_vector @@ plainto_tsquery('english', :searchTerm)",
            nativeQuery = true
    )
    Page<Book> searchBooks(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(nativeQuery = true,
    value = "select b.* from books b inner join books_authors ba on b.book_id = ba.book_id inner join authors a on ba.author_id = a.author_id where ba.author_id=:authorId ",
            countQuery ="select count(*) from books b inner join books_authors ba on b.book_id = ba.book_id inner join authors a on ba.author_id = a.author_id where ba.author_id=:authorId "
    )
    Page<Book> findBooksByAuthorId(@Param("authorId") long id, Pageable pageable);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    int deleteByIsbn(String isbn);
}
