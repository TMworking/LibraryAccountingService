package org.example.service;

import org.example.domain.Book;

import java.util.List;

public interface BookService {
    List<Book> findAllBooksByIds(List<Long> ids);
}
