package ru.gb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.dto.TaskDto;
import ru.gb.dto.TaskDtoOut;
import ru.gb.mapper.Mapper;
import ru.gb.model.Task;
import ru.gb.service.TaskService;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    private Mapper mapper;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDtoOut>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks()
                .stream()
                .map(task -> mapper.convertTaskToTaskDtoOut(task))
                .toList(),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDtoOut> addTask(@RequestBody TaskDto taskDto, Principal principal) {
        Task task;
        try {
            task = taskService.addTask(taskDto, principal.getName());
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDtoOut> getTaskById(@PathVariable Long id) {
        Task task;
        try {
            task = taskService.getTaskById(id);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<TaskDtoOut> acceptTask(@PathVariable Long id) {
        Task task;
        try {
            task = taskService.acceptTask(id);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TaskDtoOut> cancelTask(@PathVariable Long id) {
        Task task;
        try {
            task = taskService.cancelTask(id);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskDtoOut> completeTask(@PathVariable Long id) {
        Task task;
        try {
            task = taskService.completeTask(id);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

}
