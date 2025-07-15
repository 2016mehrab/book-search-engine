package com.samurai74.booksearchengine.domain.mapper;

import com.samurai74.booksearchengine.domain.dto.AuthorResponse;
import com.samurai74.booksearchengine.domain.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorResponse toAuthorResponse(Author author) {
       if(author == null) return null;
       return new AuthorResponse(
               author.getId(),
               author.getName()
       );
    }
}
