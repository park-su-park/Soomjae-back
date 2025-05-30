package com.parksupark.soomjae.server.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<String> handleMethodArgumentNotValidException(
            IllegalStateException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
