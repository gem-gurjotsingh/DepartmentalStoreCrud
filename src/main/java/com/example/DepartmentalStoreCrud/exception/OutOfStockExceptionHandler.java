package com.example.DepartmentalStoreCrud.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OutOfStockExceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleOutOfStockException(IllegalStateException ex) {
        return ResponseEntity.ok(ex.getMessage());
    }
}
