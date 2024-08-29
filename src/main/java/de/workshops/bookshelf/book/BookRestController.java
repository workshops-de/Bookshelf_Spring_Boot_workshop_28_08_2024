package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.ResourceLoader;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
class BookRestController {
    private final ObjectMapper mapper;
    private final ResourceLoader resourceLoader;

    private List<Book> books;

    public BookRestController(ObjectMapper mapper, ResourceLoader resourceLoader) {
        this.mapper = mapper;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws IOException {
        final var resource = resourceLoader.getResource("classpath:books.json");
        this.books = mapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    @GetMapping
    List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("/{isbn}")
    ResponseEntity<Book> getBookByIsbn(@PathVariable @Size(min=10, max = 14) final String isbn) {
        var first = books.stream().filter(book -> hasIsbn(book, isbn)).findFirst();
        return first.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = "author")
    Book getBookByAuthor(@RequestParam("author") @Size(min = 3) final String author) {
        return books.stream().filter(book -> hasAuthor(book, author))
                .findFirst()
                .orElseThrow(() -> new BookException("Sorry, no books for this author in the library"));
    }

    @PostMapping("/search")
    List<Book> searchBooks(@RequestBody @Valid BookSearchRequest bookSearchRequest) {
        return books.stream()
                .filter(book -> hasAuthor(book, bookSearchRequest.author())
                                             || hasIsbn(book, bookSearchRequest.isbn()))
                .toList();
    }


    @ExceptionHandler(BookException.class)
    ResponseEntity<String> handleBookException(final BookException bookException) {
        return new ResponseEntity<>(bookException.getMessage(), null, HttpStatus.I_AM_A_TEAPOT);
    }

    private boolean hasIsbn(Book book, String isbn) {
        return book.getIsbn().equals(isbn);
    }

    private boolean hasAuthor(Book book, String author) {
        return book.getAuthor().contains(author);
    }
}
