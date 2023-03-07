package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ItemStorageException;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ItemServiceImpTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserService userService;
    @Mock
    BookingRepository bookingRepository;

    ItemService service;

    Item item;
    ItemDto itemDto;

    @BeforeEach
    public void setup() {
        service = new ItemServiceImpl(userRepository, commentRepository, itemRepository, userService, bookingRepository);
        item = new Item(1, "name", "description", true, 1, null);
        itemDto = new ItemDto("name2", "description2", true, 2);
    }

    @Test
    void deleteItemTest() {
        service.deleteItem(item.getId());
        verify(itemRepository, times(1)).deleteById(item.getId());
    }

    @Test
    void saveItemTest() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        assertEquals(item, service.saveItem(1, itemDto));
    }

    @Test
    void getItemTest() {
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.ofNullable(item));
        assertEquals(item, ItemMapper.toItem(service.getItem(item.getId(), 1)));
    }

    @Test
    void getItemTestWithException() {
        when(itemRepository.findById(anyInt()))
                .thenThrow(new NoSuchElementException());
        final ItemStorageException exception = Assertions.assertThrows(
                ItemStorageException.class,
                () -> service.getItem(2, 1));
        assertEquals("Ошибка коллекции вещей: Вещи не существует", exception.getMessage());
    }

    @Test
    void getAllItemTestWithoutOwner() {
        when(itemRepository.findAll())
                .thenReturn(List.of(item));
        assertEquals(List.of(ItemMapper.toItemResponseDto(item)), service.getAllItems(0));
    }

    @Test
    void getAllItemTestWithOwner() {
        when(itemRepository.findByOwner(anyInt()))
                .thenReturn(List.of(item));
        assertEquals(List.of(ItemMapper.toItemResponseDto(item)), service.getAllItems(1));
    }

    @Test
    void updateItemTest() {
        itemDto.setAvailable(null);
        itemDto.setName(null);
        itemDto.setDescription(null);
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        assertEquals(item, service.updateItem(1, 1, itemDto));
    }

}
