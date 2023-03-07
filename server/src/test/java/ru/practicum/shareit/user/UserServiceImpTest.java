package ru.practicum.shareit.user;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserStorageException;
import ru.practicum.shareit.user.user_dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {

    @Mock
    UserRepository repository;

    UserService service;

    User user;
    UserDto userDto;

    @BeforeEach
    public void setup() {
        service = new UserServiceImpl(repository);
        user = new User(1, "name", "test@test.com");
        userDto = new UserDto("name", "test@test.com");
    }

    @Test
    void deleteUserTest() {
        service.deleteUser(user.getId());
        verify(repository, times(1)).deleteById(user.getId());
    }

    @Test
    void saveUserTest() {
        when(repository.save(any(User.class)))
                .thenReturn(user);
        assertEquals(user, service.saveUser(userDto));
    }

    @Test
    void getUserTest() {
        when(repository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        assertEquals(user, service.getUser(user.getId()));
    }

    @Test
    void getAllUserTest() {
        when(repository.findAll())
                .thenReturn(List.of(user));
        assertEquals(List.of(user), service.getAllUser());
    }

    @Test
    void getUserTestWithException() {
        when(repository.findById(anyInt()))
                .thenThrow(new NoSuchElementException());
        final UserStorageException exception = Assertions.assertThrows(
                UserStorageException.class,
                () -> service.getUser(2));
        assertEquals("Ошибка коллекции пользовтелей: Пользователя не существует", exception.getMessage());
    }

    @Test
    void updateUserTest() {
        userDto.setEmail(null);
        userDto.setName(null);
        when(repository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        when(repository.save(any(User.class)))
                .thenReturn(user);
        assertEquals(service.updateUser(user.getId(), userDto), user);
    }
}
