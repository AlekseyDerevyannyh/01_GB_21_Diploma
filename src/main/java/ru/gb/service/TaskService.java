package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import ru.gb.dto.TaskDto;
import ru.gb.model.Role;
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

    public List<Task> getAllTasks(String login) {
        return filterTasksAccordingUserRights(taskRepository.findAll(), login);
    }

    public Task getTaskById(Long id, String login) {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден"));
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Не найдено задание с идентификатором " + id));
        if (user.getRoles().contains(new Role("ADMIN"))
                || user.getRoles().contains(new Role("MANAGER"))
                || task.getUserAccepted().equals(user)
                || task.getUserIssued().equals(user)) {
            return task;
        }
        throw new AuthorizationServiceException("Не прав для просмотра данного задания данным пользователем");
    }

    public Task addTask(TaskDto taskDto, String login) {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        Task task = convertTaskDtoToTask(taskDto, login);
        if (user.getRoles().contains(new Role("USER_ISSUING"))
                && !task.getUserAccepted().equals(user)
                && task.getUserAccepted().getRoles().contains(new Role("USER_ACCEPTING"))) {
            return taskRepository.save(task);
        }
        throw new IllegalArgumentException("Ошибка при создании задания");
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task acceptTask(Long id, String login) {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден"));
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Не найдено задание с идентификатором " + id));

        if (!task.getUserAccepted().equals(user)) {
            throw new AuthorizationServiceException("Данный пользователь не может принять задание");
        }

        if (task.getStatus() == Status.CANCELED || task.getStatus() == Status.COMPLETED) {
            throw new IllegalArgumentException("Отменённое или выполненное задание не может быть принято");
        }
        task.setAcceptedAt(LocalDateTime.now());
        task.setStatus(Status.ACCEPTED);
        taskRepository.save(task);
        return task;
    }

    public Task cancelTask(Long id, String login) {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден"));
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Не найдено задание с идентификатором " + id));

        if (!task.getUserIssued().equals(user)) {
            throw new AuthorizationServiceException("Данный пользователь не может отменить задание");

        }

        if (task.getStatus() == Status.COMPLETED)
            throw new IllegalArgumentException("Отработанное задание не может быть отменено");
        task.setCanceledAt(LocalDateTime.now());
        task.setStatus(Status.CANCELED);
        taskRepository.save(task);
        return task;
    }

    public Task completeTask(Long id, String login) {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден"));
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Не найдено задание с идентификатором " + id));

        if (!task.getUserAccepted().equals(user)) {
            throw new AuthorizationServiceException("Данный пользователь не может отметить задание как выполненное");
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
                () -> new NoSuchElementException("Пользователь не найден"));
        User userIssued = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден"));

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

    private List<Task> filterTasksAccordingUserRights(List<Task> tasks, String login) {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден"));
        if (user.getRoles().contains(new Role("ADMIN"))
                || user.getRoles().contains(new Role("MANAGER"))) {
            return tasks;
        } else if (user.getRoles().contains(new Role("USER_ISSUING"))) {
            if (user.getRoles().contains(new Role("USER_ACCEPTING"))) {
                return tasks.stream()
                        .filter(task -> task.getUserIssued().equals(user) || task.getUserAccepted().equals(user))
                        .toList();
            }
            return tasks.stream()
                    .filter(task -> task.getUserIssued().equals(user))
                    .toList();
        } else if (user.getRoles().contains(new Role("USER_ACCEPTING"))) {
            return tasks.stream()
                    .filter(task -> task.getUserAccepted().equals(user))
                    .toList();
        } else {
            throw new NoSuchElementException("Не найдены задания доступные для просмотра данным пользователем");
        }
    }
}
