package com.example.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StorageException extends RuntimeException {
    private HttpStatus status;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
