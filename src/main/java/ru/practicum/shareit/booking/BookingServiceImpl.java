package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;


@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{

    private BookingRepository bookingRepository;
    private ItemService itemService;
    private UserService userService;

    @Override
    public BookingResponseDto saveBooking(int booker, BookingDto bookingDto){
        BookingResponseDto response = new BookingResponseDto(
                itemService.getItem(bookingDto.getItemId()),
                userService.getUser(booker),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                BookingStatus.WAITING);
        if(!response.getItem().getAvailable() |
                response.getStart().isAfter(response.getEnd()))
        {
            throw new BadRequestException("Ошибка при валидации бронирования");
        }
        Booking booking = BookingMapper.toBooking(booker,bookingDto);
        response.setId(bookingRepository.save(booking).getId());
        return response;
    }

    @Override
    public BookingResponseDto changeBookingStatus(int booker, Integer bookingId, Boolean approved) {
        return null;
    }
}
