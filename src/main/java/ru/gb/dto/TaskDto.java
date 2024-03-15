package ru.gb.dto;

import lombok.Getter;
import lombok.Setter;
import ru.gb.model.PowerConsumer;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskDto {
    private String name;
    private String stage;
    private String department;
    private String description;
    private List<PowerConsumer> powerConsumers = new ArrayList();
    private String userAcceptedLastName;
    private String userAcceptedFirstName;
    private String userAcceptedPatronymic;
}
