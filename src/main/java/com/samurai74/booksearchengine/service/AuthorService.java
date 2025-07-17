package com.samurai74.booksearchengine.service;

import com.samurai74.booksearchengine.domain.dto.AuthorRequest;
import com.samurai74.booksearchengine.domain.entity.Author;
import com.samurai74.booksearchengine.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AuthorService {
    Author createAuthor(AuthorRequest authorRequest);
    Page<Book> getBooksByAuthor(String authorName, PageRequest pageRequest);
    Page<Author> getAuthors(PageRequest pageRequest);
    void deleteByAuthorName(String authorName);
}
