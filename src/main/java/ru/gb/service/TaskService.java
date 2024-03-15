package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Status;
import ru.gb.model.Task;
import ru.gb.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new NoSuchElementException("Не найдено задание с идентификатором " + id);
        }
        return task;
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task acceptTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new NoSuchElementException("Не найдено задание с идентификатором " + id);
        }
        if (task.getStatus() == Status.CANCELED) {
            throw new IllegalArgumentException("Отменённое задание не может быть принято");
        }
        task.setAcceptedAt(LocalDateTime.now());
        task.setStatus(Status.ACCEPTED);
        taskRepository.save(task);
        return task;
    }

    public Task cancelTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new NoSuchElementException("Не найдено задание с идентификатором " + id);
        }
        if (task.getStatus() == Status.COMPLETED)
            throw new IllegalArgumentException("Отработанное задание не может быть отменено");
        task.setCanceledAt(LocalDateTime.now());
        task.setStatus(Status.CANCELED);
        taskRepository.save(task);
        return task;
    }

    public Task completeTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new NoSuchElementException("Не найдено задание с идентификатором " + id);
        }
        if (task.getStatus() != Status.ACCEPTED) {
            throw new IllegalArgumentException("Не принятое задание не может быть завершено");
        }
        task.setCompletedAt(LocalDateTime.now());
        task.setStatus(Status.COMPLETED);
        taskRepository.save(task);
        return task;
    }
}
