package de.workshops.bookshelf.book;

import jakarta.validation.constraints.Size;

record BookSearchRequest(@Size(min=10, max=14) String isbn, String author) {
}
