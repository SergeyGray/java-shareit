package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestResponseDto createItemRequest(@RequestHeader("X-Sharer-User-Id") int requester,
                                                    @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.saveItemRequest(ItemRequestMapper.toItemRequest(requester, itemRequestDto));
    }


    @GetMapping()
    public List<ItemRequestResponseDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") int requester) {
        return itemRequestService.getItemRequests(requester);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") int owner,
                                                          @RequestParam Integer from,
                                                          @RequestParam Integer size) {
        return itemRequestService.getAllItemsRequests(owner, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getItemRequest(@RequestHeader("X-Sharer-User-Id") int requester,
                                                 @PathVariable Integer requestId) {
        return itemRequestService.getItemRequest(requester, requestId);
    }

}
