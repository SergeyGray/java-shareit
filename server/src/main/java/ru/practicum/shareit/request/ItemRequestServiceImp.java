package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemStorageException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImp implements ItemRequestService {

    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserService userService;


    @Override
    public ItemRequestResponseDto saveItemRequest(ItemRequest itemRequest) {
        userService.getUser(itemRequest.getRequestor());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemResponseDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getItemRequests(int requester) {
        userService.getUser(requester);
        return findItemsForRequests(itemRequestRepository.findItemsRequestByRequestor(requester));
    }

    @Override
    public List<ItemRequestResponseDto> getAllItemsRequests(int owner, Integer from, Integer size) {
        userService.getUser(owner);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created").descending());
        return findItemsForRequests(itemRequestRepository.findItemsRequestsForOwner
                (owner, pageRequest).stream().collect(Collectors.toList()));
    }

    @Override
    public ItemRequestResponseDto getItemRequest(int requester, int requestId) {
        userService.getUser(requester);
        try {
            return findItemsForRequests(itemRequestRepository.findById(requestId)
                    .stream().collect(Collectors.toList())).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new ItemStorageException("Запрос на вещь не найден");
        }

    }

    private List<ItemRequestResponseDto> findItemsForRequests(List<ItemRequest> requests) {
        List<ItemRequestResponseDto> response = requests.stream()
                .map(ItemRequestMapper::toItemResponseDto).collect(Collectors.toList());
        Map<Integer, Item> itemsForRequests = itemRepository.findItemsByRequest(response
                .stream().map(ItemRequestResponseDto::getId)
                .collect(Collectors.toList())).stream().collect(Collectors.toMap(Item::getRequestId, Function.identity()));
        response.forEach(resp -> {
            if (itemsForRequests.containsKey(resp.getId())) {
                resp.getItems().add(itemsForRequests.get(resp.getId()));
            }
        });
        return response;
    }
}
