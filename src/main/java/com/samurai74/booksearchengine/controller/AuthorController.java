package com.samurai74.booksearchengine.controller;

import com.samurai74.booksearchengine.domain.dto.AuthorRequest;
import com.samurai74.booksearchengine.domain.dto.AuthorResponse;
import com.samurai74.booksearchengine.domain.dto.BookResponse;
import com.samurai74.booksearchengine.domain.mapper.AuthorMapper;
import com.samurai74.booksearchengine.domain.mapper.BookMapper;
import com.samurai74.booksearchengine.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>>  getAuthors(
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        ;
        var authorsPage =  authorService.getAuthors(PageRequest.of(page, pageSize)).map(authorMapper::toAuthorResponse);
        return ResponseEntity.ok(authorsPage);
    }


    @GetMapping("/{authorName}/books")
    public ResponseEntity<Page<BookResponse>>  getBooksByAuthor(
            @PathVariable String authorName,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        var booksPage = authorService.getBooksByAuthor(authorName,PageRequest.of(page, pageSize)).map(bookMapper::toBookResponse);
        return ResponseEntity.ok(booksPage);

    }

    @PostMapping
    public ResponseEntity<AuthorResponse>  createAuthor(
            @RequestBody @Valid AuthorRequest authorRequest
    ) {
        return ResponseEntity.ok(authorMapper.toAuthorResponse(authorService.createAuthor(authorRequest)));
    }
}
