package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponseDto {
    private int id;
    private Item item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;

    public BookingResponseDto(Item item, User booker, LocalDateTime start, LocalDateTime end, BookingStatus status) {
        this.item = item;
        this.booker = booker;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
