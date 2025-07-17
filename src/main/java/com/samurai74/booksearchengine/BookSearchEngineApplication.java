package com.samurai74.booksearchengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class BookSearchEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSearchEngineApplication.class, args);
    }

}
