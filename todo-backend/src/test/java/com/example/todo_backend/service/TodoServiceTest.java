package com.example.todo_backend.service;

import com.example.todo_backend.exception.TodoNotFoundException;
import com.example.todo_backend.model.Todo;
import com.example.todo_backend.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo(1L, "Sample Todo", false);
    }

    @Test
    void testGetTodoById_Success() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo foundTodo = todoService.getTodoById(1L);
        assertNotNull(foundTodo);
        assertEquals("Sample Todo", foundTodo.getTitle());

        verify(todoRepository).findById(1L);
    }

    @Test
    void testGetTodoById_NotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(1L));

        verify(todoRepository).findById(1L);
    }

    @Test
    void testGetAllTodos() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo));

        List<Todo> todos = todoService.getAllTodos();
        assertFalse(todos.isEmpty());
        assertEquals(1, todos.size());
        assertEquals("Sample Todo", todos.get(0).getTitle());

        verify(todoRepository).findAll();
    }

    @Test
    void testCreateTodo() {
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo createdTodo = todoService.createTodo(todo);
        assertNotNull(createdTodo);
        assertEquals("Sample Todo", createdTodo.getTitle());

        verify(todoRepository).save(todo);
    }

    @Test
    void testUpdateTodo_Success() {
        when(todoRepository.existsById(1L)).thenReturn(true);
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo updatedTodo = todoService.updateTodo(1L, todo);
        assertNotNull(updatedTodo);
        assertEquals("Sample Todo", updatedTodo.getTitle());

        verify(todoRepository).existsById(1L);
        verify(todoRepository).save(todo);
    }

    @Test
    void testUpdateTodo_NotFound() {
        when(todoRepository.existsById(1L)).thenReturn(false);

        assertThrows(TodoNotFoundException.class, () -> todoService.updateTodo(1L, todo));

        verify(todoRepository).existsById(1L);
    }

    @Test
    void testDeleteTodo_Success() {
        when(todoRepository.existsById(1L)).thenReturn(true);

        todoService.deleteTodo(1L);

        verify(todoRepository).deleteById(1L);
    }

    @Test
    void testDeleteTodo_NotFound() {
        when(todoRepository.existsById(1L)).thenReturn(false);

        assertThrows(TodoNotFoundException.class, () -> todoService.deleteTodo(1L));

        verify(todoRepository).existsById(1L);
    }
}
