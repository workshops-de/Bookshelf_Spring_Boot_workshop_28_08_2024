package de.workshops.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(BookRepositoryJdbcClient.class)
class BookRepositoryJdbcClientTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    BookRepositoryJdbcClient bookRepository;

    @Test
    void saveBook() {
        var book = new Book();
        book.setTitle("My first book");
        book.setAuthor("Birgit");
        book.setIsbn("111-1111111111");
        book.setDescription("A MUST read.");

        var save = bookRepository.save(book);
        assertThat(save.getId()).isNotNull();
    }
}