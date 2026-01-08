package com.ar.sales.point.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String NOT_FOUND = "Not Found";
    public static final String CONFLICT_ALREADY_EXISTS = "Conflict - Already exists";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), NOT_FOUND, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ConflictException ex, WebRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), CONFLICT_ALREADY_EXISTS, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), BAD_REQUEST, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, WebRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}

