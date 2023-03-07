package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestResponseDto {

    private int id;
    private String description;
    private LocalDateTime created;
    private List<Item> items = new ArrayList<>();

    public ItemRequestResponseDto(int id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}
