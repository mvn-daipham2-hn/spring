package com.example.spring.dto;

import com.example.spring.dto.customconstraint.HasDateFormatted;
import com.example.spring.errorhandler.MyValidationException;
import com.example.spring.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public @Size(min = 5, message = "Username must greater than 4 characters!") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 5, message = "Username must greater than 4 characters!") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Email must not blank!") @Email(message = "Email must be a well-formed email address!") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email must not blank!") @Email(message = "Email must be a well-formed email address!") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Birthday must not blank!") String getBirthday() {
        return birthday;
    }

    public void setBirthday(@NotBlank(message = "Birthday must not blank!") String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }

    public User toUser() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date birthdayParsed = sdf.parse(birthday);
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setBirthday(birthdayParsed);
            return user;
        } catch (ParseException e) {
            throw new MyValidationException("Invalid birthday!");
        }
    }
}
