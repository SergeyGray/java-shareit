package ru.practicum.shareit.user;

import ru.practicum.shareit.user.userDto.UserDto;
import ru.practicum.shareit.user.userDto.UserMapper;

import java.util.List;

public interface UserService {
    public User getUser(int id);
    public List<User> getAllUser();
    public User saveUser(UserDto userDto);
    public User updateUser(Integer id, UserDto userDto);
    public void deleteUser(int id);
}
