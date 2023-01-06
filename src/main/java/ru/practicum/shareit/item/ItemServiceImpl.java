package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ItemStorageException;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.item.item_dto.ItemResponseDTO;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserService userService;
    private BookingRepository bookingRepository;

    @Override
    public Item saveItem(int owner, ItemDto itemDto) {
        Item item = ItemMapper.toItem(owner, itemDto);
        userService.getUser(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Integer owner, Integer id, ItemDto itemDto) {
        userService.getUser(owner);
            Item temporaryItem = ItemMapper.toItem(getItem(id, owner));
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
    public ItemResponseDTO getItem(int id, int requestor) {
        try {
            if(itemRepository.findItemByIdAndOwner(id,requestor).isPresent()){
                ItemResponseDTO item = ItemMapper.toItemResponseDto(itemRepository.findById(id).get());
                item.setLastBooking(bookingRepository.findLastBookingForItem(id, LocalDateTime.now()).orElse(null));
                item.setNextBooking(bookingRepository.findNextBookingForItem(id,LocalDateTime.now()).orElse(null));
                return item;
            }else {
                ItemResponseDTO item = ItemMapper.toItemResponseDto(itemRepository.findById(id).get());
                return item;
            }
        } catch (NoSuchElementException e) {
            throw new ItemStorageException("Вещи не существует");
        }
    }

    @Override
    public List<ItemResponseDTO> getAllItems(int owner) {
        List<Item> items;
        if (owner > 0) {
            items = itemRepository.findByOwner(owner);
        } else{
            items = itemRepository.findAll();
        }
        List<ItemResponseDTO> itemsResponseDto = items.stream().map(item -> ItemMapper.toItemResponseDto(item))
                .collect(Collectors.toList());
        itemsResponseDto.stream().forEach(itemResponseDTO -> itemResponseDTO.setLastBooking(bookingRepository.
                findLastBookingForItem(itemResponseDTO.getId()
                ,LocalDateTime.now()).orElse(null)));
        itemsResponseDto.stream().forEach(itemResponseDTO -> itemResponseDTO.setNextBooking(bookingRepository
                .findNextBookingForItem(itemResponseDTO.getId(),LocalDateTime.now()).orElse(null)));
        return itemsResponseDto;
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
