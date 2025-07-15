package com.samurai74.booksearchengine.domain.mapper;

import com.samurai74.booksearchengine.domain.dto.AuthorResponse;
import com.samurai74.booksearchengine.domain.dto.BookResponse;
import com.samurai74.booksearchengine.domain.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;
    public BookResponse toBookResponse(Book book) {
        if(book ==null){
            return null;
        }
        Set<AuthorResponse> authors = book.getAuthors()
                .stream()
                .map(authorMapper::toAuthorResponse)
                .collect(Collectors.toSet());
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getRating(),
                book.getDescription(),
                book.getLanguage(),
                book.getPages(),
                book.getIsbn(),
                book.getBookFormat(),
                book.getPublisher(),
                book.getEdition(),
                book.getPublishDate(),
                book.getFirstPublishDate(),
                book.getLikedPercent(),
                book.getPrice(),
                authors
        );

    }
}
