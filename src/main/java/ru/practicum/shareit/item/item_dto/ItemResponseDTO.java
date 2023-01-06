package ru.practicum.shareit.item.item_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;


@Data
@AllArgsConstructor
public class ItemResponseDTO {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
    private Integer owner;
    private Booking lastBooking;
    private Booking nextBooking;

    public ItemResponseDTO(int id, String name, String description, Boolean available, Integer owner, Integer request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
        this.owner = owner;
    }
}
