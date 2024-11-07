package com.example.spring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
