package com.example.spring.errorhandler;

import lombok.Getter;

import java.util.List;

@Getter
public class MyValidationException extends RuntimeException {
    private List<ApiSubError> subErrors;

    public MyValidationException(List<ApiSubError> subErrors) {
        super("Validation errors");
        this.subErrors = subErrors;
    }

    public MyValidationException(String message) {
        super(message);
    }


}
