package com.example.spring.controller;

import com.example.spring.model.Todo;
import com.example.spring.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
    TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    ResponseEntity<List<Todo>> getTodos(
            @RequestParam(value = "title", required = false) String title
    ) {
        return ResponseEntity.ok(todoService.getTodos(title));
    }

    @GetMapping("/todos/{id}")
    ResponseEntity<?> getTodoDetails(
            @PathVariable long id
    ) {
        Todo todo = todoService.getTodoDetails(id);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/todo")
    ResponseEntity<String> updateTodo(
            @RequestBody Todo todo
    ) {
        todoService.updateTodo(todo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todo")
    ResponseEntity<String> createTodo(
            @RequestBody Todo todo
    ) {
        todoService.createTodo(todo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/todo")
    ResponseEntity<String> deleteTodo(@RequestBody long id) {
        boolean result = todoService.deleteTodo(id);
        return ResponseEntity.ok(result ? "Remove Success" : "Remove Failed");
    }
}
