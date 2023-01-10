package ru.practicum.shareit.exception;

public class BookingStorageException extends RuntimeException {
    public BookingStorageException(String message) {
        super("Ошибка коллекции бронирований: " + message);
    }
}
