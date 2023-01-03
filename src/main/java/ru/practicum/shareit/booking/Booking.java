package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
@AllArgsConstructor @NoArgsConstructor
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name ="end_date")
    private LocalDateTime end;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "booker_id")
    private int bookerId;

    @Column(name = "booking_status")
    private BookingStatus status ;

    public Booking(LocalDateTime start, LocalDateTime end, int itemId, int booker, BookingStatus status) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = booker;
        this.status = status;
    }
}
