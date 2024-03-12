package ru.gb.model;

import jakarta.persistence.*;
import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROLES")
@Data
//public class Role implements GrantedAuthority {
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "users")
    @ManyToMany(fetch = FetchType.EAGER,
        cascade = CascadeType.ALL,
        mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

//    @Override
//    public String getAuthority() {
//        return name;
//    }
}
