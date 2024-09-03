package de.workshops.bookshelf.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface BookRepositoryJpa extends JpaRepository<Book, Long> {
    List<Book> findByIsbn(String isbn);

    Book findByAuthorContaining(String author);

    List<Book> searchByIsbnOrAuthorContaining(String isbn, String author);

    @Query("select b from Book b where b.isbn=?1 or b.author like ?2")
    List<Book> searchBooks(String isbn, String author);
}
