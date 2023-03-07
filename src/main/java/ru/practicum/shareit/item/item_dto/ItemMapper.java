package ru.practicum.shareit.item.item_dto;

import ru.practicum.shareit.item.Item;

public class ItemMapper {

    public static Item toItem(Integer owner, ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                itemDto.getRequestId()
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

    public static Item toItem(ItemResponseDTO itemResponseDTO) {
        return new Item(
                itemResponseDTO.getId(),
                itemResponseDTO.getName(),
                itemResponseDTO.getDescription(),
                itemResponseDTO.getAvailable(),
                itemResponseDTO.getOwner(),
                null
        );
    }

    public static ItemResponseDTO toItemResponseDto(Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                null
        );
    }
}
