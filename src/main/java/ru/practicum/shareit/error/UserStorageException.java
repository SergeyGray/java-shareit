package ru.practicum.shareit.error;

public class UserStorageException extends RuntimeException {
    public UserStorageException(String message) {
        super("Ошибка коллекции пользовтелей: " + message);
    }
}
