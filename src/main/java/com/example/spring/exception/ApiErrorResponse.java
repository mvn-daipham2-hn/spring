package com.example.spring.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorResponse {
    private ApiError apierror;

    public ApiErrorResponse(ApiError apierror) {
        this.apierror = apierror;
    }
}
