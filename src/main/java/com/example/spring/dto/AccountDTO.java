package com.example.spring.dto;

import jakarta.validation.constraints.Size;

public class AccountDTO {
    @Size(min = 3, max = 30, message = "Username must be in 8 to 30 characters")
    String username;

    @Size(min = 3, max = 30, message = "Password must be in 8 to 30 characters")
    String password;

    @Size(min = 3, max = 30, message = "Password must be in 8 to 30 characters")
    String confirmPassword;

    public @Size(min = 3, max = 30, message = "Username must be in 8 to 30 characters") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 3, max = 30, message = "Username must be in 8 to 30 characters") String username) {
        this.username = username;
    }

    public @Size(min = 3, max = 30, message = "Password must be in 8 to 30 characters") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 3, max = 30, message = "Password must be in 8 to 30 characters") String password) {
        this.password = password;
    }

    public @Size(min = 3, max = 30, message = "Password must be in 8 to 30 characters") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@Size(min = 3, max = 30, message = "Password must be in 8 to 30 characters") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
