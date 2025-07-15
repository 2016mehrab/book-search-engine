package com.samurai74.booksearchengine.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record BookResponse(
        Long id,
        String title,
        BigDecimal rating,
        String description,
        String language,
        int pages,
        String isbn,
        String bookFormat,
        String publisher,
        String edition,
        LocalDate publishDate,
        LocalDate firstPublishDate,
        BigDecimal likedPercent,
        BigDecimal price,
        Set<AuthorResponse> authors
) {
}
