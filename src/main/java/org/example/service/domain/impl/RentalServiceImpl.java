package org.example.service.domain.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.domain.User;
import org.example.exception.OutOfStockException;
import org.example.model.Page;
import org.example.repository.RentalRepository;
import org.example.service.domain.RentalService;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    @Override
    public Rental findById(Long id) {
        log.debug("Searching rental by ID: {}", id);
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rental with id {} not found", id);
                    return new EntityNotFoundException(
                            MessageFormat.format("Rental with id {0} not found", id)
                    );
                });
        log.debug("Found rental: ID = {}", id);
        return rental;
    }

    @Override
    public Page<Rental> findPageClosedRentalsWithFilter(ClosedRentalFilterRequest request) {
        log.debug("Fetching rentals page with filter: {}", request);
        Page<Rental> page = rentalRepository.findAllClosedWithFilter(request);
        log.debug("Retrieved {} rentals (page {})", page.getContent().size(), page.getPageNumber());
        return page;
    }

    @Override
    @Transactional
    public Rental create(Book book, User user, LocalDate rentDate, Integer rentDuration) {
        log.debug("Creating new rental: UserId={}, BookId={}", user.getId(), book.getId());
        if (book.getAvailableCount() == 0) {
            log.error("No available books: Title={}", book.getName());
            throw new OutOfStockException("No available books");
        }
        if (book.getDeletedAt() != null) {
            log.error("Can't rent deleted book: Title={}", book.getName());
            throw new IllegalStateException("Can't rent deleted book");
        }
        Rental rental = new Rental();
        rental.setRentDate(rentDate);
        rental.setDuration(rentDuration);
        user.addRental(rental);
        book.addRental(rental);
        book.setAvailableCount(book.getAvailableCount() - 1);
        Rental createdRental = rentalRepository.save(rental);
        log.debug("Rental created successfully");
        return createdRental;
    }

    @Override
    @Transactional
    public Rental update(Rental rental) {
        log.debug("Updating rental: ID={}", rental.getId());
        Rental updatedRental = rentalRepository.update(rental);
        log.debug("Rental updated successfully: ID={}", rental.getId());
        return updatedRental;
    }

    @Override
    @Transactional
    public void closeRental(Rental rental) {
        log.debug("Closing rental: ID={}", rental.getId());
        rental.setReturnDate(LocalDate.now());
        Book book = rental.getBook();
        book.setAvailableCount(book.getAvailableCount() + 1);
        log.debug("Updating book available count: Title={}", book.getName());
        log.debug("Rental closed successfully: ID={}", rental.getId());
    }

    @Override
    public Page<Rental> getBookRentals(Long id, RentalFilterRequest filterRequest) {
        log.debug("Fetching book with ID={} rentals with filter: {}", id, filterRequest);
        Page<Rental> page = rentalRepository.findByBookIdWithFilter(id, filterRequest);
        log.debug("Retrieved {} rentals for book: BookId={} (page {})", page.getContent().size(), id, page.getPageNumber());
        return page;
    }

    @Override
    public Page<Rental> getUserRentals(Long id, RentalFilterRequest filterRequest) {
        return rentalRepository.findByUserIdWithFilter(id, filterRequest);
    }
}
