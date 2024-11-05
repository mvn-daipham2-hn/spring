package com.example.spring.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /// Add [Temporal] and [DateTimeFormat] annotations to get only date part with pattern
    @Temporal(TemporalType.DATE)
    /// The <input type="date"...> is always formatted yyyy-mm-dd!
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday", unique = true, nullable = false)
    private Date birthday;
}
