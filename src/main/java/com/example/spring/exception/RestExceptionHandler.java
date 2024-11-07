package com.example.spring.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.example.spring.controller")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(apiError);
        return new ResponseEntity<>(errorResponse, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMessage, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MyValidationException.class)
    protected ResponseEntity<Object> handleValidationErrors(MyValidationException ex) {
        ApiError apiError = new ApiError(ex.getHttpStatus(), ex.getMessage(), ex.getSubErrors());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    protected ResponseEntity<Object> handleStorageFileNotFound(StorageFileNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        if (ex.getCause() != null) {
            apiError.setDebugMessage(ex.getCause().getMessage());
        }
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(StorageException.class)
    protected ResponseEntity<Object> handleStoreException(StorageException ex) {
        ApiError apiError = new ApiError(
                ex.getStatus() != null ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
        if (ex.getCause() != null) {
            apiError.setDebugMessage(ex.getCause().getMessage());
        }
        return buildResponseEntity(apiError);
    }
}