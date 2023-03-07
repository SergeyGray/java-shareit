package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;

import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDto bookingDtoSave,
                                                @RequestHeader(X_SHARER_USER_ID) Long bookerId) {
        return client.createBooking(bookerId, bookingDtoSave);
    }

    @PatchMapping(path = "/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@RequestParam(name = "approved") Boolean isApproved,
                                                      @PathVariable(name = "bookingId") Long id,
                                                      @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return client.changeBookingStatus(isApproved, id, ownerId);
    }

    @GetMapping(path = "/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable(name = "bookingId", required = false) Long id,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return client.getBooking(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        try {
            BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(state);
        }
        return client.getAllBookings(bookerId, state, from, size);
    }

    @GetMapping(path = "/owner")
    public ResponseEntity<Object> getAllBookingsForItemOwner(@RequestParam(defaultValue = "ALL") String state,
                                                             @RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                             @RequestParam(defaultValue = "0") Integer from,
                                                             @RequestParam(defaultValue = "10") Integer size) {
        try {
            BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(state);
        }
        return client.getAllBookingsForItemOwner(ownerId, state, from, size);
    }

}

