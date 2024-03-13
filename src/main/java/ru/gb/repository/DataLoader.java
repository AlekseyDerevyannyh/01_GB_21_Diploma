package ru.gb.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.gb.service.TaskService;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
