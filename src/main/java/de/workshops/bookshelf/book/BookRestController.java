package de.workshops.bookshelf.book;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
class BookRestController {
    private final BookService bookService;

    BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    ResponseEntity<Book> getBookByIsbn(@PathVariable @Size(min=10, max = 14) final String isbn) {
        var first = bookService.getBookByIsbn(isbn);
        return first.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = "author")
    Book getBookByAuthor(@RequestParam("author") @Size(min = 3) final String author) {
        return bookService.getBookByAuthor(author);
    }

    @PostMapping("/search")
    List<Book> searchBooks(@RequestBody @Valid BookSearchRequest bookSearchRequest) {
        return bookService.searchBooks(bookSearchRequest);
    }

    @PostMapping
    Book createBook(@RequestBody @Valid Book book) {
        return bookService.createBook(book);
    }

    @ExceptionHandler(BookException.class)
    ResponseEntity<String> handleBookException(final BookException bookException) {
        return new ResponseEntity<>(bookException.getMessage(), null, HttpStatus.I_AM_A_TEAPOT);
    }
}
