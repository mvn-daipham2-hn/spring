package com.example.spring.service.impl;

import com.example.spring.dao.TodoRepository;
import com.example.spring.exception.ApiSubError;
import com.example.spring.exception.MyValidationException;
import com.example.spring.helper.StringHelper;
import com.example.spring.model.Todo;
import com.example.spring.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> getTodos(String title) {
        List<Todo> todos = new ArrayList<>();
        if (title == null) {
            todos.addAll(todoRepository.findAll());
        } else {
            todos.addAll(todoRepository.getTodosWithTitle(title));
        }

        return todos;
    }

    @Override
    public Todo getTodoDetails(long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            return todoOptional.get();
        } else {
            throw new EntityNotFoundException("Todo was not found for parameters {id=" + id + "}");
        }
    }

    @Override
    public void updateTodo(Todo todo) {
        getTodoDetails(todo.getId());
        validateTodoTitle(todo.getTitle());
        todoRepository.save(todo);
    }

    @Override
    public void createTodo(Todo newTodo) {
        validateTodoTitle(newTodo.getTitle());
        todoRepository.save(newTodo.todoWithoutId());
    }

    @Override
    public boolean deleteTodo(long id) {
        try {
            todoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validateTodoTitle(String title) {
        if (StringHelper.isNullOrEmpty(title)) {
            List<ApiSubError> subErrors = new ArrayList<>();
            subErrors.add(
                    new ApiSubError(
                            "user",
                            "title",
                            null,
                            "must not null"
                    )
            );
            throw new MyValidationException(HttpStatus.BAD_REQUEST, subErrors);
        }
    }

}
