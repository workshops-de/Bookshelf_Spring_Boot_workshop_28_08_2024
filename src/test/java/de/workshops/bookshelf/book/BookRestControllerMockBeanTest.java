package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookRestControllerMockBeanTest {

    @MockBean
    BookService bookServiceMock;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllBooks() throws Exception {
        when(bookServiceMock.getAllBooks()).thenReturn(List.of(new Book(), new Book()));

        var mvcResult = mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andReturn();

        var payload = mvcResult.getResponse().getContentAsString();

        List<Book> books = objectMapper.readValue(payload, new TypeReference<>(){});

        assertThat(books).hasSize(2);
    }

    @Test
    void getBookByIsbn() throws Exception {

        when(bookServiceMock.getBookByIsbn(anyString())).thenReturn(Optional.of(new Book()));

        var mvcResult = mockMvc.perform(get("/book/123-1111111111"))
                .andExpect(status().isOk())
                .andReturn();

        var payload = mvcResult.getResponse().getContentAsString();

        Book book = objectMapper.readValue(payload, new TypeReference<>(){});
        assertThat(book).isNotNull();
    }

    @Test
    void createBook() throws Exception {
        String isbn = "123-4567890123";
        String title = "My first book";
        String author = "Birgit Kratz";
        String description = "A MUST read";

        var mvcResult = mockMvc.perform(post("/book")
                        .content("""
                                {
                                    "isbn": "%s",
                                    "title": "%s",
                                    "author": "%s",
                                    "description": "%s"
                                }""".formatted(isbn, title, author, description))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var payload = mvcResult.getResponse().getContentAsString();
        Book book = objectMapper.readValue(payload, new TypeReference<>(){});
        assertThat(book).isNotNull();
    }
}