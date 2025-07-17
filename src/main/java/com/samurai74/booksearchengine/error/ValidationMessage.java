package com.samurai74.booksearchengine.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationMessage {
    private String message;
    private Map<String,List<String>> fieldErrors;
}

