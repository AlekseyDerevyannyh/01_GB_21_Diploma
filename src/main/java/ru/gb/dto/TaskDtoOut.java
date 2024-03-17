package ru.gb.dto;

import lombok.Getter;
import lombok.Setter;
import ru.gb.model.PowerConsumer;
import ru.gb.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDtoOut {
    private Long id;
    private String name;
    private String stage;
    private String department;
    private String description;
    private List<PowerConsumer> powerConsumers;
//    private UserDto userIssued;
    private LocalDateTime issuedAt;
    private UserDto userAccepted;
    private LocalDateTime acceptedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime completedAt;
    private Status status;
}
