package com.example.spring.service;

import com.example.spring.dao.TodoRepository;
import com.example.spring.helper.StringHelper;
import com.example.spring.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getTodos(String title) {
        List<Todo> todos = new ArrayList<>();
        if (title == null) {
            todos.addAll(todoRepository.findAll());
        } else {
            todos.addAll(todoRepository.getTodosWithTitle(title));
        }

        return todos;
    }

    public Optional<Todo> getTodoDetails(long id) {
        return todoRepository.findById(id);
    }

    public boolean updateTodo(Todo todo) {
        try {
            Optional<Todo> existedTodo = getTodoDetails(todo.getId());
            if (existedTodo.isEmpty()) {
                return false;
            } else {
                todoRepository.save(todo);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createTodo(String title) {
        try {
            if (StringHelper.isNullOrEmpty(title)) {
                return false;
            }

            Todo newTodo = new Todo();
            newTodo.setTitle(title);
            todoRepository.save(newTodo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteTodo(long id) {
        try {
            todoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
