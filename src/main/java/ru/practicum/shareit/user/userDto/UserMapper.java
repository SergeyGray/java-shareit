package ru.practicum.shareit.user.userDto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }

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
