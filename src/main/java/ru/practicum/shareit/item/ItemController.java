package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.userDto.UserDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;

    @PostMapping()
    public Item createItem(@RequestHeader("X-Sharer-User-Id") int owner,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.saveItem(owner,itemDto);
    }
    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") int owner,
                           @PathVariable Integer id,
                           @RequestBody ItemDto itemDto) {
        return itemService.updateItem(owner,id, itemDto);
    }
    @GetMapping("/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
    }
    @GetMapping()
    public List<Item> getAllItems(@RequestHeader(value = "X-Sharer-User-Id", required = false )int owner) {
        return itemService.getAllItems(owner);
    }
}
