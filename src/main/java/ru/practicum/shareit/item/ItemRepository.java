package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwner(Integer owner);

    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> searchItem(String text, Integer owner);

    @Query(" select i from Item i " +
            "where i.requestId in ?1")
    List<Item> findItemsByRequest(List<Integer> requestsId);

}
