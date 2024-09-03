package de.workshops.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// If you do not want to use H2 as Testdatabase, you can start a Postgres DB uand uncomment the next line
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryJpaTest {

    @Autowired
    BookRepositoryJpa bookRepositoryJpa;

    @Test
    void findAll() {
        final List<Book> books = bookRepositoryJpa.findAll();

        assertThat(books).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void findByTitle() {
        var rob = bookRepositoryJpa.findByAuthorContaining("Rob");
        assertThat(rob).isNotNull();
    }
}