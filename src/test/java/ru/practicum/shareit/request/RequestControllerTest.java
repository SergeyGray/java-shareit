package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ItemRequestService service;
    @Autowired
    MockMvc mvc;

    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setup() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("testText");
        itemRequest = new ItemRequest(1, "text", 1, LocalDateTime.now());
    }

    @Test
    void createItemRequestTest() throws Exception {
        when(service.saveItemRequest(any(ItemRequest.class)))
                .thenReturn(ItemRequestMapper.toItemResponseDto(itemRequest));
        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(itemRequest.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(itemRequest.getDescription())));
    }

    @Test
    void getItemRequestTest() throws Exception {
        when(service.getItemRequest(anyInt(), anyInt()))
                .thenReturn(ItemRequestMapper.toItemResponseDto(itemRequest));
        mvc.perform(get("/requests/{requestId}", itemRequest.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(itemRequest.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(itemRequest.getDescription())));
    }

    @Test
    void getAllItemRequestTest() throws Exception {
        when(service.getAllItemsRequests(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(ItemRequestMapper.toItemResponseDto(itemRequest),
                        ItemRequestMapper.toItemResponseDto(itemRequest)));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequest.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequest.getId())));
    }

    @Test
    void getItemRequestsTest() throws Exception {
        when(service.getItemRequests(anyInt()))
                .thenReturn(List.of(ItemRequestMapper.toItemResponseDto(itemRequest),
                        ItemRequestMapper.toItemResponseDto(itemRequest)));
        mvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequest.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequest.getId())));
    }
}
