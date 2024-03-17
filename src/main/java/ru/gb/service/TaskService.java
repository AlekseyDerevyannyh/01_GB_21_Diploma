package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.dto.TaskDto;
import ru.gb.dto.TaskDtoOut;
import ru.gb.dto.UserDto;
import ru.gb.model.Status;
import ru.gb.model.Task;
import ru.gb.model.User;
import ru.gb.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

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

    public Task addTask(TaskDto taskDto, String login) {
        return taskRepository.save(convertTaskDtoToTask(taskDto, login));
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

    private Task convertTaskDtoToTask(TaskDto taskDto, String login) {
        User userAccepted = userService.getUserByFullName(taskDto.getUserAcceptedLastName(), taskDto.getUserAcceptedFirstName(), taskDto.getUserAcceptedPatronymic()).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        User userIssued = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setStage(taskDto.getStage());
        task.setDepartment(taskDto.getDepartment());
        task.setDescription(taskDto.getDescription());
        task.setPowerConsumers(taskDto.getPowerConsumers());
        task.setUserIssued(userIssued);
        task.setUserAccepted(userAccepted);
        return task;
    }

}
