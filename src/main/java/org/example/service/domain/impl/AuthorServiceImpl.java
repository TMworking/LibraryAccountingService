package org.example.service.domain.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.repository.AuthorRepository;
import org.example.model.Page;
import org.example.service.domain.AuthorService;
import org.example.service.domain.BookService;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;

    @Override
    public Author findById(Long id) {
        log.debug("Searching author by ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Author with id {} not found", id);
                    return new EntityNotFoundException(
                            MessageFormat.format("Author with id {0} not found", id)
                    );
                });
        log.debug("Found author: ID = {}, name = {}", id, author.getName());
        return author;
    }

    @Override
    public Page<Author> findPageAuthorsWithSort(AuthorSortRequest request) {
        log.debug("Fetching authors page with sorting: {}", request);
        Page<Author> page = authorRepository.findAll(request);
        log.debug("Retrieved {} authors (page {})",
                page.getContent().size(),
                page.getPageNumber());
        return page;
    }

    @Override
    @Transactional
    public Author saveAuthor(Author author) {
        log.debug("Creating new author: Name={}", author.getName());
        Author savedAuthor = authorRepository.save(author);
        log.debug("Author created successfully: {}", savedAuthor);
        return savedAuthor;
    }

    @Override
    @Transactional
    public Author updateAuthor(Author author) {
        log.debug("Updating author: ID={}", author.getId());
        Author updatedAuthor = authorRepository.update(author);
        log.debug("Author updated successfully: {}", author);
        return updatedAuthor;
    }

    @Override
    @Transactional
    public Author addBooksToAuthor(Long id, List<Long> bookIds) {
        log.debug("Adding {} books to author: ID={}", bookIds.size(), id);
        List<Book> books = bookService.getAllBooksByIds(bookIds);
        log.debug("Found {} books for author", books.size());

        Author author = findById(id);
        books.forEach(author::addBook);

        log.debug("Successfully added books to author: ID={}", id);
        return author;
    }

    @Override
    public List<Author> getAllByIds(List<Long> ids) {
        log.debug("Fetching multiple authors by IDs: {}", ids);
        List<Author> authors = authorRepository.findAllByIds(ids);
        log.debug("Found {} authors out of requested {}", authors.size(), ids.size());
        return authors;
    }
}
