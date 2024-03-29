package ru.practicum.shareit.user.user_dto;

import ru.practicum.shareit.user.User;

public class UserMapper {

    public static User toUser(int id, UserDto userDto) {
        return new User(
                id,
                userDto.getName(),
                userDto.getEmail()
        );

    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );

    }
}
