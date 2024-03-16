package ru.gb.mapper;

import ru.gb.dto.TaskDtoOut;
import ru.gb.dto.UserDto;
import ru.gb.model.Task;
import ru.gb.model.User;

public class Mapper {
    public TaskDtoOut convertTaskToTaskDtoOut(Task task) {
        TaskDtoOut taskDtoOut = new TaskDtoOut();
        taskDtoOut.setId(task.getId());
        taskDtoOut.setName(task.getName());
        taskDtoOut.setStage(task.getStage());
        taskDtoOut.setDepartment(task.getDepartment());
        taskDtoOut.setDescription(task.getDescription());
        taskDtoOut.setPowerConsumers(task.getPowerConsumers());
        taskDtoOut.setIssuedAt(task.getIssuedAt());
        taskDtoOut.setUserAccepted(convertUserToUserDto(task.getUserAccepted()));
        taskDtoOut.setAcceptedAt(task.getAcceptedAt());
        taskDtoOut.setCanceledAt(task.getCanceledAt());
        taskDtoOut.setCompletedAt(task.getCompletedAt());
        taskDtoOut.setStatus(task.getStatus());
        return taskDtoOut;
    }

    public UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLastName(user.getLastName());
        userDto.setFirstName(user.getFirstName());
        userDto.setPatronymic(user.getPatronymic());
        return userDto;
    }
}
