package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.user_dto.UserDto;
import ru.practicum.shareit.user.user_dto.UserMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserStorageImpl userStorage;

    @Override
    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    @Override
    public List<User> getAllUser() {
        return userStorage.getAllUsers();
    }

    @Override
    public User saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return userStorage.saveUser(user);
    }

    @Override
    public User updateUser(Integer id, UserDto userDto) {
        userStorage.getUser(id);
        User user = UserMapper.toUser(id, userDto);
        return userStorage.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

}
