package ru.practicum.shareit.user;

import ru.practicum.shareit.user.userDto.UserDto;

import java.util.List;

public interface UserService {
    User getUser(int id);

    List<User> getAllUser();

    User saveUser(UserDto userDto);

    User updateUser(Integer id, UserDto userDto);

    void deleteUser(int id);
}
