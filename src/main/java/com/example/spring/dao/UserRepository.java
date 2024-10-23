package com.example.spring.dao;

import com.example.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User WHERE username LIKE %:username%")
    List<User> findAllByUsername(@Param("username") String username);
}
