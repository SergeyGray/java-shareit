package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.user_dto.UserDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestRepositoryTest {

    private final ItemRequestService service;
    private final UserService userService;
    private final ItemService itemService;
    private ItemRequest itemRequest;

    @Test
    void getAllItemsRequestsTest() {
        userService.saveUser(new UserDto("testName", "test@Email.com"));
        userService.saveUser(new UserDto("testName2", "test@Email2.com"));
        itemService.saveItem(2, new ItemDto("test", "test", true, 1));
        itemRequest = new ItemRequest(1, "test", 1, LocalDateTime.now());
        service.saveItemRequest(itemRequest);
        List<ItemRequestResponseDto> response = service.getAllItemsRequests(2, 0, 1);
        Assertions.assertEquals(itemRequest.getId(), response.get(0).getId());

    }
}
