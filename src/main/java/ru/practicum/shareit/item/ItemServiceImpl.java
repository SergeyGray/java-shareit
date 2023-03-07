package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ItemStorageException;
import ru.practicum.shareit.item.comment_dto.CommentDto;
import ru.practicum.shareit.item.comment_dto.CommentMapper;
import ru.practicum.shareit.item.comment_dto.CommentResponseDto;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.item.item_dto.ItemResponseDTO;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
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
            ItemResponseDTO item = ItemMapper.toItemResponseDto(itemRepository.findById(id).get());
            if (item.getOwner() == requestor) {
                item.setLastBooking(bookingRepository.findLastBookingForItem(id, LocalDateTime.now()).orElse(null));
                item.setNextBooking(bookingRepository.findNextBookingForItem(id, LocalDateTime.now()).orElse(null));
                item.setComments(getCommentsForItem(item));
            } else
                item.setComments(getCommentsForItem(item));
            return item;
        } catch (NoSuchElementException e) {
            throw new ItemStorageException("Вещи не существует");
        }
    }

    @Override
    public List<ItemResponseDTO> getAllItems(int owner) {
        List<Item> items;
        if (owner > 0) {
            items = itemRepository.findByOwner(owner);
        } else {
            items = itemRepository.findAll();
        }
        List<ItemResponseDTO> itemsResponseDto = items.stream().map(item -> ItemMapper.toItemResponseDto(item))
                .collect(Collectors.toList());
        List<Booking> bookingsForItems = bookingRepository.findApprovedBookingForItemId(itemsResponseDto.stream()
                .map(itemResponseDTO -> itemResponseDTO.getId()).collect(Collectors.toList()));
        for (ItemResponseDTO item : itemsResponseDto) {
            for (Booking booking : bookingsForItems) {
                if (item.getId() == booking.getItemId() && booking.getEnd().isBefore(LocalDateTime.now())) {
                    item.setLastBooking(booking);
                }
                if (item.getId() == booking.getItemId() && booking.getStart().isAfter(LocalDateTime.now())
                        && item.getNextBooking() == null) {
                    item.setNextBooking(booking);
                }
            }
        }
        return itemsResponseDto;
    }

    @Override
    public CommentResponseDto createComment(int owner, int itemId, CommentDto commentDto) {
        if (commentDto.getText().isBlank()) {
            throw new BadRequestException("Не коорректный комментарий");
        }
        if (bookingRepository.getAllBookingOnPastForBooker(owner).isEmpty()) {
            throw new BadRequestException("Не коорректный пользователь");
        }
        Comment comment = CommentMapper.toComment(commentDto, owner, itemId);
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment), userService.getUser(owner));
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

    private List<CommentResponseDto> getCommentsForItem(ItemResponseDTO item) {
        List<CommentResponseDto> response = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());
        List<User> usersForComment = userRepository.findAllById(comments.stream()
                .map(comment -> comment.getAuthorId()).collect(Collectors.toList()));
        for (Comment comment : comments) {
            for (User user : usersForComment) {
                if (comment.getAuthorId() == user.getId()) {
                    response.add(CommentMapper.toCommentResponseDto(comment, user));
                }
            }
        }
        return response;
    }

}
