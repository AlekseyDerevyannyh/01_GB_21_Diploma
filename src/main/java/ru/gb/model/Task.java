package ru.gb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание на электроснабжение
 */
@Entity
@Table(name = "TASKS")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // идентификатор задания на электроснабжение

    @Column(name = "name", nullable = false)
    private String name;        // название проектируемого объекта

    @Column(name = "stage", nullable = false)
    private String stage;       // стадия проектирования (П - проект, Р - рабочая документация)

    @Column(name = "department", nullable = false)
    private String department;  // отдел, выдающий задание

    @Column(name = "description", nullable = false)
    private String description; // описание задания

//    @OneToMany
//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PowerConsumer> powerConsumers = new ArrayList<>();       // список электропотребителей

//    @ManyToOne
//    private User userIssued;    // пользователь, выдавший задание

    @CreationTimestamp
    @Column(name = "issuedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime issuedAt;     // дата выдачи задания

    @ManyToOne
    private User userAccepted;  // пользователь, принявший задание

    @Column(name = "receivedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime acceptedAt;   // дата получения задания

    @Column(name = "canceledAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime canceledAt;     // дата отмены задания

    @Column(name = "completedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime completedAt;     // дата завершения работ по заданию

    @Column(name = "status")
//    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;  // статус задания

    public Task() {}
}
