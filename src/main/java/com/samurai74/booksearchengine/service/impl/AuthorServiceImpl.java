package com.samurai74.booksearchengine.service.impl;

import com.samurai74.booksearchengine.domain.dto.AuthorRequest;
import com.samurai74.booksearchengine.domain.entity.Author;
import com.samurai74.booksearchengine.domain.entity.Book;
import com.samurai74.booksearchengine.domain.mapper.AuthorMapper;
import com.samurai74.booksearchengine.repository.AuthorRepo;
import com.samurai74.booksearchengine.repository.BookRepo;
import com.samurai74.booksearchengine.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;

    @Override
    @Transactional
    public Author createAuthor(AuthorRequest authorRequest) {
        var author = new Author();
        author.setName(authorRequest.name());
        author.setBooks(new HashSet<>());
        return authorRepo.save(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> getBooksByAuthor(String authorName, PageRequest pageRequest) {
        var author = authorRepo.findByNameIgnoreCase(authorName).orElseThrow(()->new EntityNotFoundException("Author not found"));
        return bookRepo.findBooksByAuthorId( author.getId(),pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Author> getAuthors(PageRequest pageRequest) {
        return  authorRepo.findAll(pageRequest);
    }
}
