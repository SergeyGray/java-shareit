package ru.practicum.shareit.item.comment_dto;

import lombok.Data;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {

    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public CommentResponseDto(Comment comment, User user) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.authorName = user.getName();
        this.created = comment.getCreated();
    }
}
