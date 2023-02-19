package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.comment_dto.CommentDto;
import ru.practicum.shareit.item.comment_dto.CommentMapper;
import ru.practicum.shareit.item.item_dto.ItemDto;
import ru.practicum.shareit.item.item_dto.ItemMapper;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ItemService service;
    @Autowired
    MockMvc mvc;

    private ItemDto itemDto;
    private Item item;

    @BeforeEach
    void setup() {
        itemDto = new ItemDto("testName", "test", true, 1);
        item = new Item(1, "testName", "test", true, 1, 1);
    }

    @Test
    void saveItemTest() throws Exception {
        when(service.saveItem(anyInt(), any()))
                .thenReturn(item);
        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner", Matchers.is(item.getOwner())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId", Matchers.is(item.getRequestId())));
    }

    @Test
    void getItemTest() throws Exception {
        when(service.getItem(item.getId(), 1))
                .thenReturn(ItemMapper.toItemResponseDto(item));
        mvc.perform(get("/items/{id}", item.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner", Matchers.is(item.getOwner())));
    }

    @Test
    void getAllItemTest() throws Exception {
        Item item2 = new Item(2, "testName2", "test2", true, 1, 2);
        when(service.getAllItems(1))
                .thenReturn(List.of(ItemMapper.toItemResponseDto(item), ItemMapper.toItemResponseDto(item2)));
        mvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(item2.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is(item2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", Matchers.is(item2.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].available", Matchers.is(item2.getAvailable())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner", Matchers.is(item2.getOwner())));
    }

    @Test
    void updateItemTest() throws Exception {
        item.setId(5);
        when(service.updateItem(anyInt(), anyInt(), any(ItemDto.class)))
                .thenReturn(item);
        mvc.perform(patch("/items/{id}", item.getId())
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner", Matchers.is(item.getOwner())));
    }

    @Test
    void searchItemTest() throws Exception {
        Item item2 = new Item(2, "testName2", "test2", true, 1, 2);
        when(service.searchItem(anyString(), anyInt()))
                .thenReturn(List.of(item, item2));
        mvc.perform(get("/items/search")
                        .param("text", "testName")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(item2.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is(item2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", Matchers.is(item2.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].available", Matchers.is(item2.getAvailable())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner", Matchers.is(item2.getOwner())));
    }

    @Test
    void createCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");
        Comment comment = CommentMapper.toComment(commentDto, 1, 1);
        when(service.createComment(anyInt(), anyInt(), any(CommentDto.class)))
                .thenReturn(CommentMapper.toCommentResponseDto(comment, new User(1, "name", "email")));
        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(comment.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is(comment.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is("name")));
    }
}
