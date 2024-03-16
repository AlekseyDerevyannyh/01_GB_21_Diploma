package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Role;
import ru.gb.model.User;
import ru.gb.repository.RoleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role addRole(Role role) {
        if (roleRepository.findByName(role.getName()).isEmpty()) {
            return roleRepository.save(role);
        }
        throw new IllegalArgumentException("Данная роль уже существует");
    }

    public List<User> getRoleUsers(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Роль не найдена!"));
        return role.getUsers();
    }
}
