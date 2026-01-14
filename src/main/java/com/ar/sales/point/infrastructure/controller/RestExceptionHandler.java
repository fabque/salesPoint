package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage().replace("\"", "\\\"") : "";
        String json = String.format("{\"error\":\"%s\"}", msg);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(json);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflict(ConflictException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage().replace("\"", "\\\"") : "";
        String json = String.format("{\"error\":\"%s\"}", msg);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(json);
    }

}
