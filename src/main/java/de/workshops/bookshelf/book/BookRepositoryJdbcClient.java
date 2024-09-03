package de.workshops.bookshelf.book;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class BookRepositoryJdbcClient {
    private final JdbcClient jdbcClient;

    public BookRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    List<Book> findAll() {
        var sql = "select * from book";
        return jdbcClient.sql(sql)
                .query(new BeanPropertyRowMapper<>(Book.class))
                .list();
    }

    //    @Transactional
    Book save(Book book) {
        var sql = "INSERT INTO book (title, description, author, isbn) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .params(book.getTitle(), book.getDescription(), book.getAuthor(), book.getIsbn())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        if (Objects.nonNull(keys)) {
            var id = (Long) keys.getOrDefault("id", null);
            book.setId(id);
        }
        return book;
    }
}
