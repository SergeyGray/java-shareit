package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {
    List<Item> getAllItems(int owner);
    Item getItem(int id);
    Item saveItem(Item item);
    Item updateItem(Item item);
}
