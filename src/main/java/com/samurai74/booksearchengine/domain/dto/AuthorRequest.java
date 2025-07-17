package com.samurai74.booksearchengine.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2,max = 255 , message = "Name cannot exceed 255 characters")
        String name
) {
}
