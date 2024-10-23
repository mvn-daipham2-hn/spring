package com.example.spring.controller;

import com.example.spring.model.Todo;
import com.example.spring.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class TodoController {
    TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    ResponseEntity<List<Todo>> getTodos(
            @RequestParam(value = "title", required = false) String title) {
        return ResponseEntity.ok().body(todoService.getTodos(title));
    }

    @GetMapping("/todos/{id}")
    ResponseEntity<?> getTodoDetails(@PathVariable long id) {
        Optional<Todo> todoOptional = todoService.getTodoDetails(id);
        return ResponseEntity.ok().body(todoOptional.orElse(null));
    }

    @PutMapping("/todo")
    ResponseEntity<String> updateTodo(@RequestBody Todo todo) {
        boolean result = todoService.updateTodo(todo);
        return ResponseEntity.ok().body(result ? "Update Success" : "Update Failed");
    }

    @PostMapping("/todo")
    ResponseEntity<String> createTodo(
            @RequestBody Map<String, String> titleInfo) {
        boolean result = todoService.createTodo(titleInfo.get("title"));
        return ResponseEntity.ok().body(result ? "Create Success" : "Create Failed");
    }

    @DeleteMapping("/todo")
    ResponseEntity<String> deleteTodo(@RequestBody long id) {
        boolean result = todoService.deleteTodo(id);
        return ResponseEntity.ok().body(result ? "Remove Success" : "Remove Failed");
    }
}
