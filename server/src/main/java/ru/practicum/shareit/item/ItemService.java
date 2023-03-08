package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment_dto.CommentDto;
import ru.practicum.shareit.item.comment_dto.CommentResponseDto;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto getItem(int id, int requestor);

    List<ItemResponseDto> getAllItems(int owner);

    Item saveItem(int owner, ItemDto itemDto);

    Item updateItem(Integer owner, Integer id, ItemDto itemDto);

    void deleteItem(int id);

    List<Item> searchItem(String text, Integer owner);

    CommentResponseDto createComment(int owner, int itemId, CommentDto commentDto);

}
