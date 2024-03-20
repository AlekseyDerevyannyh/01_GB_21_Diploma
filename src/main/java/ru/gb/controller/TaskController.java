package ru.gb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
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
    public ResponseEntity<List<TaskDtoOut>> getAllTasks(Principal principal) {
        List<TaskDtoOut> tasks;
        try {
            tasks = taskService.getAllTasks(principal.getName())
                    .stream()
                    .map(task -> mapper.convertTaskToTaskDtoOut(task))
                    .toList();
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDtoOut> addTask(@RequestBody TaskDto taskDto, Principal principal) {
        Task task;
        try {
            task = taskService.addTask(taskDto, principal.getName());
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDtoOut> getTaskById(@PathVariable Long id, Principal principal) {
        Task task;
        try {
            task = taskService.getTaskById(id, principal.getName());
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AuthorizationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<TaskDtoOut> acceptTask(@PathVariable Long id, Principal principal) {
        Task task;
        try {
            task = taskService.acceptTask(id, principal.getName());
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (AuthorizationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TaskDtoOut> cancelTask(@PathVariable Long id, Principal principal) {
        Task task;
        try {
            task = taskService.cancelTask(id, principal.getName());
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (AuthorizationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskDtoOut> completeTask(@PathVariable Long id, Principal principal) {
        Task task;
        try {
            task = taskService.completeTask(id, principal.getName());
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (AuthorizationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.convertTaskToTaskDtoOut(task), HttpStatus.OK);
    }
}
