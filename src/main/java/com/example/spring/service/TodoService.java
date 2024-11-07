package com.example.spring.service;

import com.example.spring.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getTodos(String title);

    Todo getTodoDetails(long id);

    void updateTodo(Todo todo);

    void createTodo(Todo newTodo);

    boolean deleteTodo(long id);
}
