package ru.gb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
}
