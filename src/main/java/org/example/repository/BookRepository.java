package org.example.repository;

import org.example.domain.Book;
import org.example.model.Page;
import org.example.web.dto.book.request.BookFilterRequest;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(Long id);
    List<Book> findAllByIds(List<Long> ids);
    Page<Book> findAll(BookFilterRequest filter);
    Book save(Book book);
    Book update(Book book);
}
