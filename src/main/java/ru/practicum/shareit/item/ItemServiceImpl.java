package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemStorageException;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemStorage itemStorage;
    private ItemRepository itemRepository;
    private UserService userService;

    @Override
    public Item saveItem(int owner, ItemDto itemDto) {
        Item item = ItemMapper.toItem(owner, itemDto);
        userService.getUser(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Integer owner, Integer id, ItemDto itemDto) {
        userService.getUser(owner);
        Item temporaryItem = getItem(id);
        Item item = ItemMapper.toItem(owner, id, itemDto);
        if (!item.getOwner().equals(temporaryItem.getOwner()))
            throw new ItemStorageException("Неверно указан владелец вещи");
        if (item.getAvailable() == null)
            item.setAvailable(temporaryItem.getAvailable());
        if (item.getName() == null)
            item.setName(temporaryItem.getName());
        if (item.getDescription() == null)
            item.setDescription(temporaryItem.getDescription());
        return itemRepository.save(item);
    }

    @Override
    public Item getItem(int id) {
        try {
            return itemRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ItemStorageException("Вещи не существует");
        }
    }

    @Override
    public List<Item> getAllItems(int owner) {
        if (owner > 0) {
            return itemRepository.findByOwner(owner);
        } else
            return itemRepository.findAll();
    }

    @Override
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> searchItem(String text, Integer owner) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text, owner);
    }
}
