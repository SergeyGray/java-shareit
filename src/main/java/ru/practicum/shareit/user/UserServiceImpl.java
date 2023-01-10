package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserStorageException;
import ru.practicum.shareit.user.user_dto.UserDto;
import ru.practicum.shareit.user.user_dto.UserMapper;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User getUser(int id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserStorageException("Пользователя не существует");
        }
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, UserDto userDto) {
        User temporaryUser = getUser(id);
        User user = UserMapper.toUser(id, userDto);
        if (user.getEmail() == null) {
            user.setEmail(temporaryUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(temporaryUser.getName());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
