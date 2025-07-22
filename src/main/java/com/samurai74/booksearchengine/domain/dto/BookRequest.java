package com.samurai74.booksearchengine.domain.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record BookRequest(
        // Basic Book Details
        @NotBlank(message = "Title cannot be blank")
        @Size(min = 2,max = 255, message = "Title cannot exceed 255 characters")
                String title,

        @NotNull(message = "Rating cannot be null")
        @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
        @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5.0")
        BigDecimal rating,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotBlank(message = "Language cannot be blank")
        @Size(max = 255, message = "Language cannot exceed 255 characters")
        String language,

        @NotBlank(message = "ISBN cannot be blank")
        @Size(max = 255, message = "ISBN cannot exceed 255 characters")
        String isbn,

        @NotBlank(message = "Book format cannot be blank")
        @Size(max = 255, message = "Book format cannot exceed 255 characters")
        String bookFormat,

        @NotBlank(message = "Edition cannot be blank")
        @Size(max = 255, message = "Edition cannot exceed 255 characters")
        String edition,

        @NotNull(message = "Pages cannot be null")
        @Min(value = 1, message = "Book must have at least 1 page")
        int pages,

        @NotBlank(message = "Publisher cannot be blank")
        @Size(max = 255, message = "Publisher cannot exceed 255 characters")
        String publisher,

        @NotNull(message = "Publish date cannot be null")
        @PastOrPresent(message = "Publish date cannot be in the future")
        LocalDate publishDate,

        @NotNull(message = "First publish date cannot be null")
        @PastOrPresent(message = "First publish date cannot be in the future")
        LocalDate firstPublishDate,

        @NotNull(message = "Liked percentage cannot be null")
        @DecimalMin(value = "0.0", inclusive = true, message = "Liked percentage must be at least 0.0")
        @DecimalMax(value = "100.0", inclusive = true, message = "Liked percentage must be at most 100.0")
        BigDecimal likedPercent,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
        BigDecimal price,

        @NotNull(message = "Authors cannot be null")
        @Size(min = 1, message = "Book must have at least one author")
        Set<String> authorNames
) {
}
