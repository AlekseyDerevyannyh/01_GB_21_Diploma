package ru.gb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "stage")
    private String stage;

    @Column(name = "department")
    private String department;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "task")
    private List<Task> tasks = new ArrayList<>();

    @Column(name = "user_issued")
    private User userIssued;

    @Column(name = "issuedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime issuedAt;

    @Column(name = "user_received")
    private User userReceived;

    @Column(name = "receivedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime receivedAt;

}
