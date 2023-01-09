package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id where b.id = ?1 and i.owner = ?2")
    Optional<Booking> findBookingByOwnerItem(int bookingId, int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where b.id = ?2 and (i.owner = ?1 or b.bookerId = ?1)")
    Optional<Booking> getBooking(int requestor, int bookingId);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1) ORDER BY (b.start) desc ")
    List<Booking> getAllBooking(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where b.bookerId = ?1 and b.status = 'REJECTED' ORDER BY (b.start) desc ")
    List<Booking> getAllBookingRejected(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where b.bookerId = ?1 and b.status = 'WAITING' ORDER BY (b.start) desc ")
    List<Booking> getAllBookingWaiting(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1) and current_timestamp < b.start ORDER BY (b.start) desc ")
    List<Booking> getAllBookingOnFuture(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1) and current_timestamp > b.end ORDER BY (b.start) desc ")
    List<Booking> getAllBookingOnPast(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (i.owner = ?1 or b.bookerId = ?1)" +
            " and current_timestamp > b.start and current_timestamp < b.end ORDER BY (b.start) desc ")
    List<Booking> getAllBookingOnCurrent(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (b.bookerId = ?1) and current_timestamp > b.end ORDER BY (b.start) desc ")
    List<Booking> getAllBookingOnPastForBooker(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where (b.bookerId = ?1 or i.owner = ?1 ) and current_timestamp > b.end ORDER BY (b.start) desc ")
    List<Booking> getAllBookingOnPastForBookerOrOwner(int requestor);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 ORDER BY (b.start) desc  ")
    List<Booking> getAllBookingsForItemOwner(int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = 'REJECTED'  ORDER BY (b.start) desc  ")
    List<Booking> getAllBookingsForItemOwnerRejected(int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = 'WAITING'  ORDER BY (b.start) desc  ")
    List<Booking> getAllBookingsForItemOwnerWaiting(int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and current_timestamp < b.start   ORDER BY (b.start) desc ")
    List<Booking> getAllBookingsForItemOwnerFuture(int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and current_timestamp > b.end   ORDER BY (b.start) desc ")
    List<Booking> getAllBookingsForItemOwnerPast(int owner);

    @Query("SELECT b from Booking b join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and " +
            "current_timestamp > b.start and current_timestamp < b.end   ORDER BY (b.start) desc ")
    List<Booking> getAllBookingsForItemOwnerCurrent(int owner);

    @Query(value = "SELECT * from bookings b  left join items i on b.item_id = i.id" +
            " where i.id = ?1 and  b.end_date < ?2 order by b.end_date desc limit 1", nativeQuery = true)
    Optional<Booking> findLastBookingForItem(int itemId, LocalDateTime dateTime);

    @Query(value = "SELECT * from bookings b  left join items i on b.item_id = i.id" +
            " where i.id = ?1 and  b.start_date > ?2 order by b.start_date desc limit 1", nativeQuery = true)
    Optional<Booking> findNextBookingForItem(int itemId, LocalDateTime dateTime);
}
