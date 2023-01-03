package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

public class BookingMapper {

    public static Booking toBooking (int booker, BookingDto bookingDto){
    return new Booking(
            bookingDto.getStart(),
            bookingDto.getEnd(),
            bookingDto.getItemId(),
            booker,
            BookingStatus.WAITING
    );
    }

}
