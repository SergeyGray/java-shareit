package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto saveItemRequest(ItemRequest itemRequest);

    List<ItemRequestResponseDto> getItemRequests(int requester);

    List<ItemRequestResponseDto> getAllItemsRequests(int owner, Integer from, Integer size);

    ItemRequestResponseDto getItemRequest(int requester, int requestId);
}
