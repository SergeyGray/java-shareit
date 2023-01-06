package ru.practicum.shareit.item;

import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemResponseDTO;

import java.util.List;

public interface ItemService {

    ItemResponseDTO getItem(int id, int requestor);

    List<ItemResponseDTO> getAllItems(int owner);

    Item saveItem(int owner, ItemDto itemDto);

    Item updateItem(Integer owner, Integer id, ItemDto itemDto);

    void deleteItem(int id);

    List<Item> searchItem(String text, Integer owner);

}
