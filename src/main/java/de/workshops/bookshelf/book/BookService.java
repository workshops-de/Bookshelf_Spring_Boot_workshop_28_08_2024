package de.workshops.bookshelf.book;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class BookService {
    private final BookRepositoryJdbcTemplate bookRepository;

    BookService(BookRepositoryJdbcTemplate bookRepository) {
        this.bookRepository = bookRepository;
    }

    List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    Optional<Book> getBookByIsbn(final String isbn) {
        return bookRepository.findAll().stream().filter(book -> hasIsbn(book, isbn)).findFirst();

    }

    Book getBookByAuthor(final String author) {
        return bookRepository.findAll().stream().filter(book -> hasAuthor(book, author))
                .findFirst()
                .orElseThrow(() -> new BookException("Sorry, no books for this author in the library"));
    }

    List<Book> searchBooks(BookSearchRequest bookSearchRequest) {
        return bookRepository.findAll().stream()
                .filter(book -> hasAuthor(book, bookSearchRequest.author())
                                || hasIsbn(book, bookSearchRequest.isbn()))
                .toList();
    }

    private boolean hasIsbn(Book book, String isbn) {
        return book.getIsbn().equals(isbn);
    }

    private boolean hasAuthor(Book book, String author) {
        return book.getAuthor().contains(author);
    }

    public Book createBook(@Valid Book book) {
        return bookRepository.save(book);
    }
}
