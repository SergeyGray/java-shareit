package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookingServiceImpl service;
    @Autowired
    MockMvc mvc;

    private BookingDto bookingDto;
    private Booking booking;

    @BeforeEach
    void setup() {
        booking = new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
                , 1, 1, BookingStatus.WAITING);
        bookingDto = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBookingTest() throws Exception {
        when(service.saveBooking(any(Booking.class)))
                .thenReturn(BookingMapper.toBookingResponseDto(booking));
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(booking.getId()), Integer.class));
    }

    @Test
    void changeBookingStatusTest() throws Exception {
        when(service.changeBookingStatus(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(BookingMapper.toBookingResponseDto(booking));
        mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(booking.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(BookingStatus.WAITING.name())));
    }

    @Test
    void getBookingTest() throws Exception {
        when(service.getBooking(anyInt(), anyInt()))
                .thenReturn(BookingMapper.toBookingResponseDto(booking));
        mvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(booking.getId()), Integer.class));
    }

    @Test
    void getAllBookingTest() throws Exception {
        when(service.getAllBookings(anyInt(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(List.of(BookingMapper.toBookingResponseDto(booking)));
        mvc.perform(get("/bookings/")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(1), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(booking.getId()), Integer.class));
    }

    @Test
    void getAllBookingExceptionTest() throws Exception {
        when(service.getAllBookings(anyInt(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(List.of(BookingMapper.toBookingResponseDto(booking)));
        mvc.perform(get("/bookings/")
                        .param("state", "ERROR")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Unknown state: ERROR")));
    }

    @Test
    void getAllBookingsForItemOwnerTest() throws Exception {
        when(service.getAllBookingsForItemOwner(anyInt(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(List.of(BookingMapper.toBookingResponseDto(booking)));
        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(booking.getId()), Integer.class));
    }

    @Test
    void getAllBookingsForItemOwnerExceptionTest() throws Exception {
        when(service.getAllBookingsForItemOwner(anyInt(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(List.of(BookingMapper.toBookingResponseDto(booking)));
        mvc.perform(get("/bookings/owner")
                        .param("state", "ERROR")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Unknown state: ERROR")));
    }
}
