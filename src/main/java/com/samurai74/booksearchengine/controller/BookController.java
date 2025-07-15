package com.samurai74.booksearchengine.controller;

import com.samurai74.booksearchengine.domain.dto.BookRequest;
import com.samurai74.booksearchengine.domain.dto.BookResponse;
import com.samurai74.booksearchengine.domain.mapper.BookMapper;
import com.samurai74.booksearchengine.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getBooks(
            @RequestParam(name="search") String searchTerm,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "page") int page
            ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var booksPage = bookService.searchBooks(searchTerm, pageable);
        var books =booksPage.map(bookMapper::toBookResponse);
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(
            @PathVariable(required = true) String isbn
    ) {
        return ResponseEntity.ok(bookMapper.toBookResponse(bookService.getBookByIsbn(isbn)) );
    }

    @DeleteMapping(path = "/{isbn}")
    public ResponseEntity<Void> deleteBookByIsbn(
            @PathVariable(required = true) String isbn
    ){
       bookService.deleteByIsbn(isbn);
       return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest bookRequest) {
        return ResponseEntity.ok(bookMapper.toBookResponse(bookService.createBook(bookRequest)) );
    }

}
