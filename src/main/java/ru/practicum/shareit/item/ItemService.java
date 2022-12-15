package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    public Item getItem(int id);
    public List<Item> getAllItems(int owner);
    public Item saveItem(int owner,ItemDto itemDto);
    public Item updateItem(Integer owner,Integer id, ItemDto itemDto);
    public void deleteItem(int id);
}
