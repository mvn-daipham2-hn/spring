package com.example.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MyValidationException extends RuntimeException {
    private HttpStatus httpStatus;
    private List<ApiSubError> subErrors;

    public MyValidationException(HttpStatus httpStatus, List<ApiSubError> subErrors) {
        super("Validation errors");
        this.httpStatus = httpStatus;
        this.subErrors = subErrors;
    }

    public MyValidationException(String message) {
        super(message);
    }
}
