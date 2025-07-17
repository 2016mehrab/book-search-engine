package com.samurai74.booksearchengine.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorMessage {
    private int statusCode;
    private Instant timestamp;
    private String message;
    private String description;
}
