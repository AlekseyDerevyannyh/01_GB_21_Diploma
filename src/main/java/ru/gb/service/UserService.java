package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Role;
import ru.gb.model.User;
import ru.gb.repository.RoleRepository;
import ru.gb.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        if (userRepository.findByLogin(user.getLogin()).isEmpty()
                && userRepository.findByLastNameAndFirstNameAndPatronymic(
                user.getLastName(), user.getFirstName(), user.getPatronymic())
                .isEmpty()) {
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("Данный пользователь уже существует");
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Не найден пользователь с идентификатором " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> getUserByFullName(String lastName, String firstName, String patronymic) {
        return userRepository.findByLastNameAndFirstNameAndPatronymic(lastName, firstName, patronymic);
    }

    public User addRoleToUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new NoSuchElementException("Роль не найдена!"));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + userId +
                    " уже содержит роль с идентификатором " + roleId);
        }
        user.addRole(role);
        userRepository.save(user);
        return user;
    }

    public User addRoleToUser(String lastName, String firstName, String patronymic, String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new NoSuchElementException("Роль не найдена!"));
        User user = userRepository.findByLastNameAndFirstNameAndPatronymic(lastName, firstName, patronymic)
                .orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + user.getId() +
                    " уже содержит роль с идентификатором " + role.getId());
        }
        user.addRole(role);
        userRepository.save(user);
        return user;
    }

    public User removeRoleFromUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new NoSuchElementException("Роль не найдена!"));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        if (!user.getRoles().contains(role)) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + userId +
                    " не содержит роль с идентификатором " + roleId);
        }
        user.removeRole(role);
        userRepository.save(user);
        return user;
    }

    public User changeUserPassword(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Пользователь не найден!"));
        user.setPassword(password);
        userRepository.save(user);
        return user;
    }
}
