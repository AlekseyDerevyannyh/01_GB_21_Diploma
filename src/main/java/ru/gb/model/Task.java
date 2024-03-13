package ru.gb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание на электроснабжение
 */
@Entity
@Table(name = "TASKS")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // идентификатор задания на электроснабжение

    @Column(name = "name")
    private String name;        // название проектируемого объекта

    @Column(name = "stage")
    private String stage;       // стадия проектирования (П - проект, Р - рабочая документация)

    @Column(name = "department")
    private String department;  // отдел, выдающий задание

    @Column(name = "description")
    private String description; // описание задания

//    @OneToMany
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PowerConsumer> powerConsumers = new ArrayList<>();       // список электропотребителей

    @ManyToOne
    private User userIssued;    // пользователь, выдавший задание

    @Column(name = "issuedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime issuedAt;     // дата выдачи задания

    @ManyToOne
    private User userReceived;  // пользователь, получивший задание

    @Column(name = "receivedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime receivedAt;   // дата получения задания

    public Task(String name, String stage, String department, String description, User userIssued, User userReceived) {
        this.name = name;
        this.stage = stage;
        this.department = department;
        this.description = description;
        this.userIssued = userIssued;
        this.userReceived = userReceived;
        this.issuedAt = LocalDateTime.now();
    }
}
