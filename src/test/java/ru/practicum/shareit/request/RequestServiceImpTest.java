package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImpTest {

    @Mock
    ItemRequestRepository repository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserService userService;

    ItemRequestService service;

    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;

    @BeforeEach
    public void setup() {
        service = new ItemRequestServiceImp(repository, itemRepository, userService);
        itemRequest = new ItemRequest(1, "test", 1, LocalDateTime.now());
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("test2");
    }

    @Test
    void saveItemRequestTest() {
        when(repository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        assertEquals(ItemRequestMapper.toItemResponseDto(itemRequest), service.saveItemRequest(itemRequest));
    }

    @Test
    void getItemRequestsTest() {
        Item item = new Item(1, "testName", "testDescription", true, 2, 1);
        when(repository.findItemsRequestByRequestor(anyInt()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findItemsByRequest(List.of(itemRequest.getId())))
                .thenReturn(List.of(item));
        ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.toItemResponseDto(itemRequest);
        itemRequestResponseDto.setItems(List.of(item));
        assertEquals(itemRequestResponseDto, service.getItemRequests(itemRequest
                .getRequestor()).get(0));
    }

    @Test
    void getItemRequestTest() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(itemRequest));
        assertEquals(ItemRequestMapper.toItemResponseDto(itemRequest), service.getItemRequest(1, 1));
    }
}
