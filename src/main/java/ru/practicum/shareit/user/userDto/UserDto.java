package ru.practicum.shareit.user.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
    private String name;
    @Email
    @NotBlank
    private String email;
}
