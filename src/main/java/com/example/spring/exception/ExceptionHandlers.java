package com.example.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLIntegrityConstraintViolationException;

//@ControllerAdvice(basePackages = "com.example.spring.controller")
@ControllerAdvice
public class ExceptionHandlers {
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(apiError);
        return new ResponseEntity<>(errorResponse, apiError.getStatus());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private ModelAndView handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        return new ModelAndView("internal-server-error");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Object> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        String errorMessage = "Maximum upload size exceeded, keep file(s) equal or less than 1MB";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, ex);
        return buildResponseEntity(apiError);
    }
}
