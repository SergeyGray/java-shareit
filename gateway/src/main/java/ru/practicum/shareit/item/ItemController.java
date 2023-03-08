package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) long userId,
                                         @Valid @RequestBody ItemDto itemDto) {
        return client.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable long itemId,
                                         @RequestBody ItemDto itemDto) {
        return client.update(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(value = X_SHARER_USER_ID, required = false) long userId) {
        return client.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(value = X_SHARER_USER_ID, required = false) long userId) {
        return client.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(value = X_SHARER_USER_ID, required = false) long userId,
                                         @RequestParam String text) {
        if (text.isBlank()) {
            return ResponseEntity.of(Optional.of(Collections.emptyList()));
        }
        return client.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                @PathVariable Long itemId,
                                                @RequestBody CommentDto commentDto) {
        if (commentDto.getText().isBlank()) {
            throw new BadRequestException("Не коорректный комментарий");
        }
        return client.createComment(userId, itemId, commentDto);
    }
}
