package ru.practicum.shareit.exception;

public class UserStorageException extends RuntimeException {
    public UserStorageException(String message) {
        super("Ошибка коллекции пользовтелей: " + message);
    }
}
