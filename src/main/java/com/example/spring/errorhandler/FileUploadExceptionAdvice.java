package com.example.spring.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Object> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        String errorMessage = "Maximum upload size exceeded, keep file(s) equal or less than 1MB";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, ex);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(apiError);
        return new ResponseEntity<>(errorResponse, apiError.getStatus());
    }
}
