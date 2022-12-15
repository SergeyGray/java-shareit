package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    private ItemStorage itemStorage;
    private UserStorage userStorage;

    @Override
    public Item saveItem(int owner, ItemDto itemDto){
        userStorage.getUser(owner);
        Item item = ItemMapper.toItem(owner, itemDto);
        return itemStorage.saveItem(item);
    }
    @Override
    public Item updateItem(Integer owner, Integer id, ItemDto itemDto) {
        userStorage.getUser(owner);
        itemStorage.getItem(id);
        Item item = ItemMapper.toItem(owner,id,itemDto);
        return itemStorage.updateItem(item);
    }
    @Override
    public Item getItem(int id) {
        return itemStorage.getItem(id);
    }

    @Override
    public List<Item> getAllItems(int owner) {
        return itemStorage.getAllItems(owner);
    }

    @Override
    public void deleteItem(int id) {

    }
}
