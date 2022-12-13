package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User getUser(int id);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(int id);
}
