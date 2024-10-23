package com.example.spring.dao;

import com.example.spring.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("FROM Todo WHERE title LIKE %:title%")
    List<Todo> getTodosWithTitle(@Param("title") String title);
}
