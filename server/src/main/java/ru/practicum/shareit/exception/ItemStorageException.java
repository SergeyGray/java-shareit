package ru.practicum.shareit.exception;

public class ItemStorageException extends RuntimeException {
    public ItemStorageException(String message) {
        super("Ошибка коллекции вещей: " + message);
    }
}
