package de.workshops.bookshelf;

import de.workshops.bookshelf.book.BookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ProblemDetail handleBookException(final BookException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("BookException");
        problemDetail.setInstance(URI.create("http://localhost:8080/bookException.html"));
        return problemDetail;
    }
}
