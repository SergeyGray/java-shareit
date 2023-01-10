package ru.practicum.shareit.item.comment_dto;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, int owner, int itemId) {
        return new Comment(commentDto.getText(), itemId, owner, LocalDateTime.now());
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment, User user) {
        return new CommentResponseDto(comment, user);
    }
}
