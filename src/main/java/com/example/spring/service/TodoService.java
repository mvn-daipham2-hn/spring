package com.example.spring.service;

import com.example.spring.dao.TodoRepository;
import com.example.spring.errorhandler.ApiSubError;
import com.example.spring.errorhandler.ApiValidationError;
import com.example.spring.errorhandler.MyValidationException;
import com.example.spring.helper.StringHelper;
import com.example.spring.model.Todo;
import jakarta.persistence.EntityNotFoundException;
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

    public Todo getTodoDetails(long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            return todoOptional.get();
        } else {
            throw new EntityNotFoundException("Todo was not found for parameters {id=" + id + "}");
        }
    }

    private void validateTodoTitle(String title) {
        if (StringHelper.isNullOrEmpty(title)) {
            List<ApiSubError> subErrors = new ArrayList<>();
            subErrors.add(new ApiValidationError(
                    "user",
                    "title",
                    null,
                    "must not null")
            );
            throw new MyValidationException(subErrors);
        }
    }

    public void updateTodo(Todo todo) {
        getTodoDetails(todo.getId());
        validateTodoTitle(todo.getTitle());
        todoRepository.save(todo);
    }

    public void createTodo(Todo newTodo) {
        validateTodoTitle(newTodo.getTitle());
        todoRepository.save(newTodo.todoWithoutId());
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
