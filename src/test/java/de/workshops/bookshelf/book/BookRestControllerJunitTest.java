package de.workshops.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRestControllerJunitTest {

    @Mock
    BookService bookServiceMock;

    @InjectMocks
    BookRestController bookRestController;

    @Captor
    ArgumentCaptor<String> isbnCaptor;

    @Test
    void getAllBooks() {
        when(bookServiceMock.getAllBooks()).thenReturn(List.of(new Book(), new Book()));

        final List<Book> books = bookRestController.getAllBooks();

        assertThat(books).hasSize(2);
    }

    @Test
    void getBookByIsbn() {
        String isbn = "123";

        when(bookServiceMock.getBookByIsbn(isbnCaptor.capture())).thenReturn(Optional.of(new Book()));

        var bookByIsbn = bookRestController.getBookByIsbn(isbn);

        assertThat(bookByIsbn).isNotNull();
        assertThat(isbnCaptor.getValue()).isEqualTo(isbn);
    }

    @Test
    void getBookByAuthorThrowsBookException() {
        doThrow(BookException.class).when(bookServiceMock).getBookByAuthor(anyString());

        assertThatExceptionOfType(BookException.class).isThrownBy(() -> bookRestController.getBookByAuthor("Birgit"));
    }
}