package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.domain.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAllBooksByIds(List<Long> ids) {
        return bookRepository.findAllByIds(ids);
    }
}
