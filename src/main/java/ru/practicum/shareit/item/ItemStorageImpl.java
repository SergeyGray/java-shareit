package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemStorageException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {

    private final List<Item> items = new ArrayList<>();
    private int id = 0;

    @Override
    public List<Item> getAllItems(int owner) {
        if (owner > 0)
            return items.stream().filter(item -> item.getOwner() == owner).collect(Collectors.toList());
        else
            return items;
    }

    @Override
    public Item getItem(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        throw new ItemStorageException("Вещи не существует");
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(++id);
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item temporaryItem = getItem(item.getId());
        if (item.getOwner() != temporaryItem.getOwner())
            throw new ItemStorageException("Неверно указан владелец вещи");
        items.remove(getItem(temporaryItem.getId()));
        if (item.getAvailable() == null)
            item.setAvailable(temporaryItem.getAvailable());
        if (item.getName() == null)
            item.setName(temporaryItem.getName());
        if (item.getDescription() == null)
            item.setDescription(temporaryItem.getDescription());
        items.add(item);
        return item;
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isEmpty())
            return new ArrayList<Item>();
        return items.stream().filter(item -> item.getAvailable()).
                filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase())).
                collect(Collectors.toList());
    }
}
