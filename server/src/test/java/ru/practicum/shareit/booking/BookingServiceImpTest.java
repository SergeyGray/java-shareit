package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingStorageException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImpTest {
    @Mock
    BookingRepository repository;
    @Mock
    ItemService itemService;
    @Mock
    UserService userService;

    BookingService service;

    Booking booking;
    BookingDto bookingDto;
    BookingResponseDto bookingResponseDto;
    User user;
    Item item;

    @BeforeEach
    public void setup() {
        service = new BookingServiceImpl(repository, itemService, userService);
        booking = new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                1, 1, BookingStatus.WAITING);
        bookingDto = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        item = new Item(1, "text", "description", true, 2, null);
        user = new User(1, "testName", "test@email.com");
        bookingResponseDto = new BookingResponseDto(1, item, user,
                booking.getStart(), booking.getEnd(), BookingStatus.WAITING);
    }

    @Test
    void saveBookingTest() {
        when(repository.save(booking))
                .thenReturn(booking);
        when(itemService.getItem(anyInt(), anyInt()))
                .thenReturn(ItemMapper.toItemResponseDto(item));
        when(userService.getUser(anyInt()))
                .thenReturn(user);
        booking.setBookerId(1);
        assertEquals(bookingResponseDto, service.saveBooking(booking));
    }

    @Test
    void saveBookingBadRequestExceptionTest() {
        item.setAvailable(false);
        when(itemService.getItem(anyInt(), anyInt()))
                .thenReturn(ItemMapper.toItemResponseDto(item));
        when(userService.getUser(anyInt()))
                .thenReturn(user);
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> service.saveBooking(booking));
        assertEquals("Unknown state: Ошибка при валидации бронирования", exception.getMessage());
    }

    @Test
    void saveBookingBookingExceptionTest() {
        item.setOwner(1);
        when(itemService.getItem(anyInt(), anyInt()))
                .thenReturn(ItemMapper.toItemResponseDto(item));
        when(userService.getUser(anyInt()))
                .thenReturn(user);
        final BookingStorageException exception = Assertions.assertThrows(
                BookingStorageException.class,
                () -> service.saveBooking(booking));
        assertEquals("Ошибка коллекции бронирований: Владелец вещи не может бронировать вещь",
                exception.getMessage());
    }

    @Test
    void getBookingTest() {
        when(repository.getBooking(anyInt(), anyInt()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemService.getItem(anyInt(), anyInt()))
                .thenReturn(ItemMapper.toItemResponseDto(item));
        when(userService.getUser(anyInt()))
                .thenReturn(user);
        assertEquals(bookingResponseDto, service.getBooking(1, 1));
    }
}
