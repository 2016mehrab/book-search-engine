package com.samurai74.booksearchengine.service.impl;

import com.samurai74.booksearchengine.domain.dto.BookRequest;
import com.samurai74.booksearchengine.domain.entity.Author;
import com.samurai74.booksearchengine.domain.entity.Book;
import com.samurai74.booksearchengine.repository.AuthorRepo;
import com.samurai74.booksearchengine.repository.BookRepo;
import com.samurai74.booksearchengine.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;

    @Override
    public Page<Book> searchBooks(String searchTerm,Pageable pageable) {
        if(searchTerm == null || searchTerm.isEmpty()) {
            return bookRepo.findAll(pageable);
        }
        return bookRepo.searchBooks(searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookByIsbn(String isbn) {
        return bookRepo.findByIsbn(isbn).orElseThrow(()-> new EntityNotFoundException("book with isbn: "+ isbn+" not found"));
    }

    @Override
    @Transactional
    public void deleteByIsbn(String isbn) {
        int deletedCount = bookRepo.deleteByIsbn(isbn);
        if(deletedCount==0) throw new EntityNotFoundException("book with isbn: "+ isbn+" not found");

    }

    @Override
    @Transactional
    public Book createBook(BookRequest bookRequest) {
        if (bookRepo.existsByIsbn(bookRequest.isbn())) 
            throw new IllegalArgumentException("book with isbn: "+ bookRequest.isbn()+" already exists");

        Book book = getBook(bookRequest);
        Set<Author> authors =  bookRequest.authorNames().stream()
                .map((name)-> authorRepo.findByNameIgnoreCase(name).orElseGet(()->{
                    var author = new Author();
                    author.setName(name);
                    author.setBooks(new HashSet<>());
                    return author;
                }))
                .collect(Collectors.toSet());
        for (var author: authors) book.addAuthor(author);
        return bookRepo.save(book);
    }

    private static Book getBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.title());
        book.setRating(bookRequest.rating());
        book.setDescription(bookRequest.description());
        book.setLanguage(bookRequest.language());
        book.setIsbn(bookRequest.isbn());
        book.setBookFormat(bookRequest.bookFormat());
        book.setEdition(bookRequest.edition());
        book.setPages(bookRequest.pages());
        book.setPublisher(bookRequest.publisher());
        book.setPublishDate(bookRequest.publishDate());
        book.setFirstPublishDate(bookRequest.firstPublishDate());
        book.setLikedPercent(bookRequest.likedPercent());
        book.setPrice(bookRequest.price());
        book.setAuthors(new HashSet<>());
        return book;
    }
}
