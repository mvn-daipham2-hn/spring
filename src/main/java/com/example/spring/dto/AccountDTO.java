package com.example.spring.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountDTO {
    @Size(min = 3, max = 30, message = "Username must be in 3 to 30 characters")
    String username;

    @Size(min = 3, max = 30, message = "Password must be in 3 to 30 characters")
    String password;

    @Size(min = 3, max = 30, message = "Password must be in 3 to 30 characters")
    String confirmPassword;
}
