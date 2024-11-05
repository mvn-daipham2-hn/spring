package com.example.spring.dao;

import com.example.spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User WHERE username LIKE %:username%")
    Page<User> findAllByUsername(@Param("username") String username, Pageable pageable);

    /// This method is the same as "findAllByUsername" above, but simpler
    Page<User> findAllByUsernameContainingIgnoreCase(String search, Pageable pageable);

    List<User> findUsersByBirthday(Date date);
}
