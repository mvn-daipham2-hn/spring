package com.example.spring.errorhandler;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<ApiSubError> subErrors;

    public ValidationException(List<ApiSubError> subErrors) {
        super("Validation errors");
        this.subErrors = subErrors;
    }
}
