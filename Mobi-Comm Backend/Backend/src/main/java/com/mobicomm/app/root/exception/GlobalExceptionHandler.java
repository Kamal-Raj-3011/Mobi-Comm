package com.mobicomm.app.root.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }

    // Handle Category Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse("Resource Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle Duplicate Category
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(DuplicateResourceException ex) {
        return buildErrorResponse("Duplicate Resource", ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handle Invalid Requests (e.g., activating an already active category)
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequest(InvalidRequestException ex) {
        return buildErrorResponse("Invalid Request", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        return buildErrorResponse("Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    
}
