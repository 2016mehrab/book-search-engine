package com.samurai74.booksearchengine.service.impl;

import com.samurai74.booksearchengine.domain.entity.Book;
import com.samurai74.booksearchengine.repository.BookRepo;
import com.samurai74.booksearchengine.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;

    @Override
    public Page<Book> searchBooks(String searchTerm,Pageable pageable) {
        if(searchTerm == null || searchTerm.isEmpty()) {
            return bookRepo.findAll(pageable);
        }
        return bookRepo.searchBooks(searchTerm, pageable);
    }
}
