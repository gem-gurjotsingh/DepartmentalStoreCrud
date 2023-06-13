package com.example.DepartmentalStoreCrud.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidEmail {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidEmailException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}