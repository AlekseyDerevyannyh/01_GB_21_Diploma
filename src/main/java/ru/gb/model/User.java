package ru.gb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // идентификатор сотрудника

    @Column(name = "login", nullable = false, unique = true)
    private String login;       // логин сотрудника

    @Column(name = "password", nullable = false)
    private String password;    // пароль сотрудника

    @Column(name = "last_name", nullable = false)
    private String lastName;    // фамилия сотрудника

    @Column(name = "first_name", nullable = false)
    private String firstName;   // имя сотрудника

    @Column(name = "patronymic", nullable = false)
    private String patronymic;  // отчество сотрудника

    @ManyToMany(fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();   // список ролей сотрудника

    public User() {
    }

    public User(String login, String password, String lastName, String firstName, String patronymic) {
        this.login = login;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getLogin(), user.getLogin()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getPatronymic(), user.getPatronymic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getPassword(), getLastName(), getFirstName(), getPatronymic());
    }
}
