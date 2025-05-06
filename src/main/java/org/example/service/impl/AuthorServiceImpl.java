package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.repository.AuthorRepository;
import org.example.model.Page;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Author with id {0} not found", id)
                ));
    }

    @Override
    public Page<Author> findPageAuthorsWithSort(AuthorSortRequest request) {
        return authorRepository.findAll(request);
    }

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(Author author) {
        return authorRepository.update(author);
    }

    @Override
    @Transactional
    public Author addBooksToAuthor(Long id, List<Long> bookIds) {
        List<Book> books = bookService.findAllBooksByIds(bookIds);
        Author author = findById(id);
        books.forEach(author::addBook);
        return author;
    }
}
