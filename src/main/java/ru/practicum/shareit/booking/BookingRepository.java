package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b from Booking b  join Item i on b.itemId = i.id where i.id in ?1 and b.status = 'APPROVED' ORDER BY (b.start)")
    List<Booking> findApprovedBookingForItemId(List<Integer> itemId);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id where b.id = ?1 and i.owner = ?2")
    Optional<Booking> findBookingByOwnerItem(int bookingId, int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where b.id = ?2 and (i.owner = ?1 or b.bookerId = ?1)")
    Optional<Booking> getBooking(int requestor, int bookingId);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1) ORDER BY (b.start) desc ")
    Page<Booking> getAllBooking(int requestor, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where b.bookerId = ?1 and b.status = 'REJECTED' ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingRejected(int requestor, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where b.bookerId = ?1 and b.status = 'WAITING' ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingWaiting(int requestor, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1) and current_timestamp < b.start ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingOnFuture(int requestor, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1) and current_timestamp > b.end ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingOnPast(int requestor, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1)" +
            " and current_timestamp > b.start and current_timestamp < b.end ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingOnCurrent(int requestor, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (b.bookerId = ?1) and current_timestamp > b.end ORDER BY (b.start) desc ")
    List<Booking> getAllBookingOnPastForBooker(int requestor);


    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 ORDER BY (b.start) desc  ")
    Page<Booking> getAllBookingsForItemOwner(int owner, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = 'REJECTED'  ORDER BY (b.start) desc  ")
    Page<Booking> getAllBookingsForItemOwnerRejected(int owner, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = 'WAITING'  ORDER BY (b.start) desc  ")
    Page<Booking> getAllBookingsForItemOwnerWaiting(int owner, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and current_timestamp < b.start   ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingsForItemOwnerFuture(int owner, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and current_timestamp > b.end   ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingsForItemOwnerPast(int owner, Pageable page);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and " +
            "current_timestamp > b.start and current_timestamp < b.end   ORDER BY (b.start) desc ")
    Page<Booking> getAllBookingsForItemOwnerCurrent(int owner, Pageable page);

    @Query(value = "SELECT * from bookings b  left join items i on b.item_id = i.id" +
            " where i.id = ?1 and  b.end_date < ?2 order by b.end_date desc limit 1", nativeQuery = true)
    Optional<Booking> findLastBookingForItem(int itemId, LocalDateTime dateTime);

    @Query(value = "SELECT * from bookings b  left join items i on b.item_id = i.id" +
            " where i.id = ?1 and  b.start_date > ?2 order by b.start_date desc limit 1", nativeQuery = true)
    Optional<Booking> findNextBookingForItem(int itemId, LocalDateTime dateTime);
}
