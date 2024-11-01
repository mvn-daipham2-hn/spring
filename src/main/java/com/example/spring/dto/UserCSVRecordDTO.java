package com.example.spring.dto;

import com.example.spring.model.User;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;

/// If you’re using a field mapping mechanism like [BeanWrapperFieldSetMapper] in Spring Batch
/// to map CSV data directly to [UserCSVRecordDTO], it relies on a no-argument constructor
/// to instantiate [UserCSVRecordDTO] before setting each field.
@NoArgsConstructor
public class UserCSVRecordDTO extends UserDTO {
    @PositiveOrZero(message = "Id must be greater than or equal to 0")
    private String id;

    public UserCSVRecordDTO(String id, String username, String email, String birthday) {
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

    @Override
    public String toString() {
        return "UserCSVRecordDTO{" +
                "id='" + id + '\'' +
                ", username='" + getUsername() + '\'' +
                ", email='" + getUsername() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                '}';
    }
}
