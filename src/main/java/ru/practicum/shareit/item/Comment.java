package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    @Column(name = "item_id")
    private int itemId;
    @Column(name = "author_id")
    private int authorId;
    private LocalDateTime created;

    public Comment(String text, int itemId, int authorId, LocalDateTime created) {
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.created = created;
    }
}
