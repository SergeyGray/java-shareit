package ru.practicum.shareit.item.item_dto;

import ru.practicum.shareit.item.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item toItem(Integer owner, ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                null
        );
    }

    public static Item toItem(Integer owner, int id, ItemDto itemDto) {
        return new Item(
                id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                null
        );
    }
}