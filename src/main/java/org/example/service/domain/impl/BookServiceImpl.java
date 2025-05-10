package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.exception.BookDeletionException;
import org.example.exception.NotFoundException;
import org.example.model.Page;
import org.example.repository.BookRepository;
import org.example.repository.RentalRepository;
import org.example.service.domain.BookService;
import org.example.web.dto.book.request.BookFilterRequest;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RentalRepository rentalRepository;

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Book with id {0} not found", id)
                ));
    }

    @Override
    public Page<Book> findBooksWithFilter(BookFilterRequest filterRequest) {
        return bookRepository.findAll(filterRequest);
    }

    @Override
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        return bookRepository.update(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);

        long activeRentals = rentalRepository.countActiveRentalsByBookId(id);
        if (activeRentals > 0) {
            throw new BookDeletionException("Can't delete book with active rentals");
        }
        book.setDeletedAt(LocalDateTime.now());
    }

    @Override
    public Page<Rental> getBookRentals(Long id, RentalFilterRequest filterRequest) {
        return rentalRepository.findByBookIdWithFilter(id, filterRequest);
    }

    @Override
    public List<Book> findAllBooksByIds(List<Long> ids) {
        return bookRepository.findAllByIds(ids);
    }
}
