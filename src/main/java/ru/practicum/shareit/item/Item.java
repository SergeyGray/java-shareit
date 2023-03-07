package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private Integer owner;
    @Column(name = "request_id")
    private Integer requestId;

    public Item(String name, String description, Boolean available, Integer owner, Integer request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = request;
    }

}
