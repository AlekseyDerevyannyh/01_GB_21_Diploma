package ru.gb.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.gb.model.Role;
import ru.gb.model.User;
import ru.gb.service.RoleService;
import ru.gb.service.TaskService;
import ru.gb.service.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {
    private final TaskService taskService;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        roleService.addRole(new Role("ADMIN"));
        roleService.addRole(new Role("MANAGER"));
        roleService.addRole(new Role("USER_ISSUING"));
        roleService.addRole(new Role("USER_ACCEPTING"));

        userService.addUser(new User("admin", "admin", "Ivanov", "Ivan", "Ivanovich"));
        userService.addUser(new User("manager", "admin", "Petrov", "Pyotr", "Petrovich"));
        userService.addUser(new User("user_issuing", "admin", "Sokolov", "Pavel", "Aleksandrovich"));
        userService.addUser(new User("user_accepting", "admin", "Sidorov", "Aleksey", "Konstantinovich"));

        userService.addRoleToUser("Ivanov", "Ivan", "Ivanovich", "ADMIN");
        userService.addRoleToUser("Petrov", "Pyotr", "Petrovich", "MANAGER");
        userService.addRoleToUser("Sokolov", "Pavel", "Aleksandrovich", "USER_ISSUING");
        userService.addRoleToUser("Sidorov", "Aleksey", "Konstantinovich", "USER_ACCEPTING");

    }
}
