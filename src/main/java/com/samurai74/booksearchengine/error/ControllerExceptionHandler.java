package com.samurai74.booksearchengine.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationMessage handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        var errors = new HashMap<String,List<String>>();
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        for(var  fieldError : fieldErrors){
            errors.putIfAbsent(fieldError.getField(),new ArrayList<>());
            errors.get(fieldError.getField()).add(fieldError.getDefaultMessage());
        }

//        ex.getAllErrors().forEach(error-> errors.add(error.getDefaultMessage()));

//        log.error("field errors: {}",fielderrors);

        return new ValidationMessage("Validation Failed",errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.error("Failed to read HTTP message for request to {}: {}",
                request.getRequestURI(), ex.getMessage());

        String detailMessage = "The request body is malformed or could not be read.";
        if (ex.getCause() != null) {
            // ex.getCause() often contains more specific details from Jackson (e.g., MismatchedInputException)
            detailMessage += " Specific cause: " + ex.getCause().getMessage();
        }

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                "Invalid Request Body",
                detailMessage
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        log.error("Resource not found for request to {}: {}",
                request.getRequestURI(), exception.getMessage());

        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                Instant.now(),
                "Resource Not Found",
                request.getRequestURI() + ": " + exception.getMessage()
        );

    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        log.error("Invalid argument for request to {}: {}",
                request.getRequestURI(), exception.getMessage());

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                "Invalid Request Input",
                request.getRequestURI() + ": " + exception.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(Exception exception, HttpServletRequest request) {
        log.error("An unexpected internal server error of type {} occurred during request to {}: {}",exception.getClass().getName(),
                request.getRequestURI(), exception.getMessage(), exception);
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now(),
                "An unexpected internal server error occurred.",
                request.getRequestURI() + ": Please try again later. If the issue persists, contact support."
        );

    }
}
