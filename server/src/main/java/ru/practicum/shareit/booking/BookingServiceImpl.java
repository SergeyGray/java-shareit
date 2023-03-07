package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingStorageException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private ItemService itemService;
    private UserService userService;

    @Override
    public BookingResponseDto saveBooking(Booking booking) {
        BookingResponseDto response = mapperToResponse(booking);
        if (!response.getItem().getAvailable() |
                response.getStart().isAfter(response.getEnd())) {
            throw new BadRequestException("Ошибка при валидации бронирования");
        }
        if (response.getItem().getOwner().equals(response.getBooker().getId())) {
            throw new BookingStorageException("Владелец вещи не может бронировать вещь");
        }
        response.setId(bookingRepository.save(booking).getId());
        return response;
    }

    @Override
    public BookingResponseDto changeBookingStatus(int owner, Integer bookingId, Boolean approved) {
        BookingStatus status;
        Optional<Booking> booking;
        status = (approved) ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking = bookingRepository.findBookingByOwnerItem(bookingId, owner);

        if (booking.isEmpty()) {
            throw new BookingStorageException("Неккоретный владелец вещи");
        }
        if (status == booking.get().getStatus()) {
            throw new BadRequestException("Одиннаковый статус");
        }
        booking.get().setStatus(status);
        return saveBooking(booking.get());
    }

    @Override
    public BookingResponseDto getBooking(int requestor, Integer bookingId) {
        Optional<Booking> booking = bookingRepository.getBooking(requestor, bookingId);
        if (booking.isEmpty()) {
            throw new BookingStorageException("Бронирование не найдено");
        }
        return mapperToResponse(booking.get());
    }

    @Override
    public List<BookingResponseDto> getAllBookings(int requestor, BookingState state, int from, int size) {
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from, size);
        switch (state) {
            case ALL:
                bookings = bookingRepository.getAllBooking(requestor, page);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllBookingRejected(requestor, page);
                break;
            case WAITING:
                bookings = bookingRepository.getAllBookingWaiting(requestor, page);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllBookingOnFuture(requestor, page);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllBookingOnCurrent(requestor, page);
                break;
            case PAST:
                bookings = bookingRepository.getAllBookingOnPast(requestor, page);
                break;
            default:
                throw new BadRequestException(state.toString());
        }
        if (bookings.isEmpty())
            throw new BookingStorageException("Не найдены бронирования согласно условиям");
        return bookings.stream().map(this::mapperToResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllBookingsForItemOwner(int owner, BookingState state, int from, int size) {
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from, size);
        switch (state) {
            case ALL:
                bookings = bookingRepository.getAllBookingsForItemOwner(owner, page);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllBookingsForItemOwnerFuture(owner, page);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllBookingsForItemOwnerRejected(owner, page);
                break;
            case WAITING:
                bookings = bookingRepository.getAllBookingsForItemOwnerWaiting(owner, page);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllBookingsForItemOwnerCurrent(owner, page);
                break;
            case PAST:
                bookings = bookingRepository.getAllBookingsForItemOwnerPast(owner, page);
                break;
            default:
                throw new BadRequestException(state.toString());
        }
        if (bookings.isEmpty())
            throw new BookingStorageException("Не найдены бронирования согласно условиям");
        return bookings.stream().map(this::mapperToResponse).collect(Collectors.toList());
    }

    private BookingResponseDto mapperToResponse(Booking booking) {
        BookingResponseDto response = BookingMapper.toBookingResponseDto(booking);
        response.setItem(ItemMapper.toItem(itemService.getItem(booking.getItemId(), 0)));
        response.setBooker(userService.getUser(booking.getBookerId()));
        return response;
    }
}
