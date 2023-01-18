package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private int itemId;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
