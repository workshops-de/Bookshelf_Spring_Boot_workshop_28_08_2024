package de.workshops.bookshelf.book;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryNamedJdbcTemplate {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookRepositoryNamedJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Book> findAll() {
        var sql = "select * from book";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
    }

    Book save(Book book) {
        var sql = "INSERT INTO book (title, description, author, isbn) VALUES (:title, :description, :author, :isbn)";

        var parameters = new MapSqlParameterSource();
        parameters.addValue("title", book.getTitle());
        parameters.addValue("description", book.getDescription());
        parameters.addValue("author", book.getAuthor());
        parameters.addValue("isbn", book.getIsbn());

        jdbcTemplate.update(sql, parameters);
        return book;
    }
}
