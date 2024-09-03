package de.workshops.bookshelf.book;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:bookshelf")
class BookRestControllerTest {

    @Autowired
    BookRestController bookRestController;

    @Test
    void getAllBooks() {
        var allBooks = bookRestController.getAllBooks();

        assertThat(allBooks).hasSize(3);
    }

    @Test
    void getBookByIsbn_validIsbn() {
        var bookByIsbn = bookRestController.getBookByIsbn("978-3826655487");
        assertThat(bookByIsbn.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookByIsbn.getBody()).isNotNull();
    }

    @Test
    void getBookByIsbn_isbnNotExisting() {
        var bookByIsbn = bookRestController.getBookByIsbn("111-1111111111");
        assertThat(bookByIsbn.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookByIsbn.getBody()).isNull();
    }

    @Test
    void getBookByIsbn_invalidIsbn() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()-> bookRestController.getBookByIsbn("111-111111111111111"));
    }

    @Test
    void getBookByAuthor_authorNotExisting() {
        assertThatExceptionOfType(BookException.class).isThrownBy(() -> bookRestController.getBookByAuthor("Birgit"));
    }

}