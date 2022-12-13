package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.UserStorageException;

import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

@Component
public class UserStorageImpl implements UserStorage {

    private final List<User> users = new ArrayList<>();
    private int id =0;

    @Override
    public List<User> getAllUsers() {
        return users;
    }
    @Override
    public User getUser(int id) {
        for(User user:users){
            if(user.getId() == id){
                return user;
            }
        }
        throw new UserStorageException("Пользователя не существует");
    }

    @Override
    public User saveUser(User user) {
        checkDuplicateEmail(user.getEmail());
        user.setId(++id);
        users.add(user);
        return user;
    }
    @Override
    public User updateUser(User user) {
        checkDuplicateEmail(user.getEmail());
        User temporaryUser = getUser(user.getId());
        users.remove(getUser(temporaryUser.getId()));
        if(user.getEmail() == null){
            user.setEmail(temporaryUser.getEmail());
        }
        if(user.getName() == null){
            user.setName(temporaryUser.getName());
        }
        users.add(user);
        return user;
    }
    @Override
    public void deleteUser(int id) {
        User user = getUser(id);
        users.remove(user);
    }

    private void checkDuplicateEmail(String email) {
        if (email == null)
                return;
        if(users.stream().anyMatch(user -> email.equals(user.getEmail())))
            throw new DuplicateFormatFlagsException(email);
    }
}
