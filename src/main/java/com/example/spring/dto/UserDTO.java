package com.example.spring.dto;

import com.example.spring.exception.MyValidationException;
import com.example.spring.model.User;
import com.example.spring.validation.HasDateFormatted;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Size(min = 5, message = "Username must greater than 4 characters!")
    private String username;

    @NotBlank(message = "Email must not blank!")
    @Email(message = "Email must be a well-formed email address!")
    private String email;

    @NotBlank(message = "Birthday must not blank!")
    @HasDateFormatted(message = "Birthday must be in format `dd-MM-yyyy`!")
    private String birthday;

    public User toUser() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setBirthday(sdf.parse(birthday));
            return user;
        } catch (ParseException e) {
            throw new MyValidationException("Invalid birthday!");
        }
    }
}
