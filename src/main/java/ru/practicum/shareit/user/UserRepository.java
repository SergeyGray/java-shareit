package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.user_dto.UserDto;

public interface UserRepository extends JpaRepository<User, Integer> {
}
