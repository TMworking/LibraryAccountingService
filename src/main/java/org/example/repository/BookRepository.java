package org.example.repository;

import org.example.model.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(Long id);
    List<Book> findAllByIds(List<Long> ids);
    List<Book> findAll(int page, int size);
    Book save(Book book);
    Book update(Book book);
    void delete(Book book);
    Long countAll();
    // TODO: filter should supports: find by author, find by title, sorting,
//    List<Book> findAll(Specification<Book> spec);
    // Do this as course repo
    // QueryDSL
}
