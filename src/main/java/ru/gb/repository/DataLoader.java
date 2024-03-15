package ru.gb.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.gb.model.User;
import ru.gb.service.TaskService;
import ru.gb.service.UserService;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final TaskService taskService;
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.addUser(new User("login", "password", "Ivanov", "Ivan", "Ivanovich"));
    }
}
