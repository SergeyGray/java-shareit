package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private BookingService bookingService;

    @PostMapping()
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") int booker,
                                            @RequestBody BookingDto bookingDto) {
        return bookingService.saveBooking(BookingMapper.toBooking(booker, bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto changeBookingStatus(@RequestHeader("X-Sharer-User-Id") int owner,
                                                  @PathVariable Integer bookingId,
                                                  @RequestParam Boolean approved) {
        return bookingService.changeBookingStatus(owner, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") int requestor,
                                         @PathVariable(required = false) Integer bookingId) {
        return bookingService.getBooking(requestor, bookingId);
    }

    @GetMapping()
    public List<BookingResponseDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") int requestor,
                                                   @RequestParam String state,
                                                   @RequestParam Integer from,
                                                   @RequestParam Integer size) {
        return bookingService.getAllBookings(requestor, BookingState.valueOf(state), from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsForItemOwner(@RequestHeader("X-Sharer-User-Id") int requestor,
                                                               @RequestParam String state,
                                                               @RequestParam Integer from,
                                                               @RequestParam Integer size) {
        return bookingService.getAllBookingsForItemOwner(requestor, BookingState.valueOf(state), from, size);
    }

}
