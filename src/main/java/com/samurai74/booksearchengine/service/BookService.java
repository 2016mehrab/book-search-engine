package com.samurai74.booksearchengine.service;

import com.samurai74.booksearchengine.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> searchBooks(String searchTerm,Pageable pageable);
}
