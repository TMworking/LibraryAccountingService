package org.example.service.domain;

import org.example.domain.Book;
import org.example.model.Page;
import org.example.web.dto.book.request.BookFilterRequest;

import java.util.List;

public interface BookService {
    Book findById(Long id);
    Page<Book> findBooksWithFilter(BookFilterRequest filterRequest);
    Book createBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Long id);
    List<Book> getAllBooksByIds(List<Long> ids);
}
