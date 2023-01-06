package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    BookingResponseDto saveBooking (Booking booking);

    BookingResponseDto changeBookingStatus(int owner, Integer bookingId, Boolean approved);

    BookingResponseDto getBooking (int requester, Integer bookingId);
    List<BookingResponseDto> getAllBookings (int requester, BookingState state);
    List<BookingResponseDto> getAllBookingsForItemOwner (int owner, BookingState state);
}
