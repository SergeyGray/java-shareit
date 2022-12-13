package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserStorageImpl userStorage;

    public User getUser(int id){
        return userStorage.getUser(id);
    }
    public List<User> getAllUser(){
        return userStorage.getAllUsers();
    }
    public User saveUser(User user){
        return userStorage.saveUser(user);
    }
    public User updateUser(Integer id, User user){
        user.setId(id);
        return userStorage.updateUser(user);
    }
    public void deleteUser(int id){
        userStorage.deleteUser(id);
    }

}
