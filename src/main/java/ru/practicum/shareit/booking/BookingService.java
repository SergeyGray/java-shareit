package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

public interface BookingService {

    public BookingResponseDto saveBooking (int booker, BookingDto bookingDto);

    BookingResponseDto changeBookingStatus(int booker, Integer bookingId, Boolean approved);
}
