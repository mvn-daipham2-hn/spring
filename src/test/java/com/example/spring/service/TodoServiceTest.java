package com.example.spring.service;

import com.example.spring.dao.TodoRepository;
import com.example.spring.model.Todo;
import com.example.spring.service.impl.TodoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoServiceImpl todoService;

    @Test
    void whenGetTodos_shouldReturnList() {
        // create mock data
        List<Todo> mockTodos = new ArrayList<>();
        List<Todo> mockTodosWithTitle = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockTodos.add(new Todo((long) i, "Todo" + i));
        }
        for (int i = 0; i < 3; i++) {
            mockTodosWithTitle.add(new Todo((long) i, "abc" + i));
        }

        String title = "abc";

        // define behavior for repository
//        when(todoRepository.findAll()).thenReturn(mockTodos);
        when(todoRepository.getTodosWithTitle(title)).thenReturn(mockTodos);

        // call service method
        List<Todo> actualTodos = todoService.getTodos(title);
//        List<Todo> actualTodosWithTitle = todoService.getTodos(null);

        // assert the result
        assertThat(actualTodos.size()).isEqualTo(mockTodos.size());
//        assertThat(actualTodosWithTitle.size()).isEqualTo(mockTodosWithTitle.size());

        // ensure repository is called
//        verify(todoRepository).findAll();
        verify(todoRepository).getTodosWithTitle(title);
    }

    @Test
    void whenGetInvalidOne_shouldThrowException() {
        long invalidId = 1L;

        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        assertThatThrownBy(()-> todoService.getTodoDetails(invalidId))
//                .isInstanceOf(MyValidationException.class);

        assertThatThrownBy(() -> todoService.getTodoDetails(invalidId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(todoRepository).findById(any(Long.class));
    }
}
