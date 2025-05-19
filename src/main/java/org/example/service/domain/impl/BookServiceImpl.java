package org.example.service.domain.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Book;
import org.example.exception.BookDeletionException;
import org.example.model.Page;
import org.example.repository.BookRepository;
import org.example.repository.RentalRepository;
import org.example.service.domain.BookService;
import org.example.web.dto.book.request.BookFilterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RentalRepository rentalRepository;

    @Override
    public Book findById(Long id) {
        log.debug("Searching book by ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with id {} not found", id);
                    return new EntityNotFoundException(
                            MessageFormat.format("Book with id {0} not found", id)
                    );
                });
        log.debug("Found book: ID = {}, name = {}", id, book.getName());
        return book;
    }

    @Override
    public Page<Book> findBooksWithFilter(BookFilterRequest filterRequest) {
        log.debug("Fetching book page with filter: {}", filterRequest);
        Page<Book> page = bookRepository.findAll(filterRequest);
        log.debug("Retrieved {} books (page {})", page.getContent().size(), page.getPageNumber());
        return page;
    }

    @Override
    @Transactional
    public Book createBook(Book book) {
        log.debug("Creating new book: Title={}", book.getName());
        Book savedBook = bookRepository.save(book);
        log.debug("Book created successfully: {}", savedBook.getName());
        return savedBook;
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        log.debug("Updating book: ID={}", book.getId());
        Book updatedBook = bookRepository.update(book);
        log.debug("Book updated successfully: {}", book);
        return updatedBook;
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        log.debug("Deleting book: ID={}", id);
        Book book = findById(id);

        log.debug("Counting active rentals for book: ID={}", id);
        long activeRentals = rentalRepository.countActiveRentalsByBookId(id);
        if (activeRentals > 0) {
            log.error("Book {} have active rentals, can't delete this book", book.getName());
            throw new BookDeletionException("Can't delete book with active rentals");
        }
        book.setDeletedAt(LocalDateTime.now());
        log.debug("Book {} deleted at: {}", book.getName(), book.getDeletedAt());
    }

    @Override
    public List<Book> getAllBooksByIds(List<Long> ids) {
        log.debug("Fetching multiple books by IDs: {}", ids);
        List<Book> books = bookRepository.findAllByIds(ids);
        log.debug("Found {} books out of requested {}", books.size(), ids.size());
        return books;
    }
}
