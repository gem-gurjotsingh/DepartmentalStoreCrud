package com.example.DepartmentalStoreCrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class EntityIdNotFound {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException() {
        return new ResponseEntity<>("Does not exist with given id", HttpStatus.NOT_FOUND);
    }
}