package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.user_dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserServiceImpl userService;
    @Autowired
    MockMvc mvc;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setup() {
        userDto = new UserDto("test", "test@test.com");
        user = new User(1, "test", "test@test.com");
    }

    @Test
    void saveUserTest() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(user);
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail())));
    }

    @Test
    void getUserTest() throws Exception {
        when(userService.getUser(user.getId()))
                .thenReturn(user);
        mvc.perform(get("/users/{id}", user.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail())));
    }

    @Test
    void getAllUserTest() throws Exception {
        User user2 = new User(2, "test2", "test2@test2.com");
        when(userService.getAllUser())
                .thenReturn(List.of(user, user2));
        mvc.perform(get("/users/", user.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(user.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is(user.getEmail())));
    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.updateUser(anyInt(), any(UserDto.class)))
                .thenReturn(user);
        mvc.perform(patch("/users/{id}", user.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail())));
    }

    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/{id}", user.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1))
                .deleteUser(user.getId());
    }
}
