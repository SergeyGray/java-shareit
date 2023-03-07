package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findItemsRequestByRequestor(int requesterId);

    @Query("SELECT i FROM ItemRequest i WHERE i.requestor <> ?1")
    Page<ItemRequest> findItemsRequestsForOwner(int owner, Pageable page);

}
