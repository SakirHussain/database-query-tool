package com.assignment.datasets.controller;

import com.assignment.datasets.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.URI;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle BadRequestException - return 400 Bad Request
     */
    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage()
        );
        problemDetail.setType(URI.create("/bad-request"));
        problemDetail.setTitle("Bad Request");
        return problemDetail;
    }

    /**
     * Handle MethodArgumentNotValidException - return 422 Unprocessable Entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Validation failed: " + errors
        );
        problemDetail.setType(URI.create("/validation-error"));
        problemDetail.setTitle("Validation Error");
        return problemDetail;
    }

    /**
     * Handle generic exceptions - return 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred"
        );
        problemDetail.setType(URI.create("/internal-error"));
        problemDetail.setTitle("Internal Server Error");
        
        // In development, you might want to include the actual error message
        // problemDetail.setDetail(ex.getMessage());
        
        return problemDetail;
    }
} 