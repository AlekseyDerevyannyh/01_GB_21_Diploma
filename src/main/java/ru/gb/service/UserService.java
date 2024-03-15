package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Role;
import ru.gb.model.User;
import ru.gb.repository.RoleRepository;
import ru.gb.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User addUser(User user) {
        if (userRepository.findByLastNameAndFirstNameAndPatronymic(
                user.getLastName(), user.getFirstName(), user.getPatronymic())
                .isEmpty()) {
            return userRepository.save(user);
        }
        return user;
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> getUserByFullName(String lastName, String firstName, String patronymic) {
        return userRepository.findByLastNameAndFirstNameAndPatronymic(lastName, firstName, patronymic);
    }

    public void addRoleToUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new NoSuchElementException("Роль не найдена!"));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        user.addRole(role);
        userRepository.save(user);
    }
}
