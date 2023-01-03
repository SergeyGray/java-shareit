package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private BookingService bookingService;
    @PostMapping()
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") int booker,
                                         @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.saveBooking(booker,bookingDto);
    }
    @PatchMapping
    public BookingResponseDto changeBookingStatus(@RequestHeader("X-Sharer-User-Id") int booker,
                                           @PathVariable Integer bookingId,
                                           @RequestParam Boolean approved) {
        return bookingService.changeBookingStatus(booker, bookingId,approved);
    }

}
