package com.example.spring.dto;

import com.example.spring.model.User;
import jakarta.validation.constraints.PositiveOrZero;


public class UserRecordDTO extends UserDTO {
    @PositiveOrZero(message = "Id must be greater than or equal to 0")
    private String id;

    public UserRecordDTO(String id, String username, String email, String birthday) {
        super(username, email, birthday);
        this.id = id;
    }

    public @PositiveOrZero(message = "Id must be greater than or equal to 0") String getId() {
        return id;
    }

    public void setId(@PositiveOrZero(message = "Id must be greater than or equal to 0") String id) {
        this.id = id;
    }

    @Override
    public User toUser() {
        User user = super.toUser();
        user.setId(Long.parseLong(this.id));
        return user;
    }
}
