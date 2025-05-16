package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.domain.User;
import org.example.exception.NotFoundException;
import org.example.exception.OutOfStockException;
import org.example.model.Page;
import org.example.repository.RentalRepository;
import org.example.service.domain.RentalService;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;


    @Override
    public Rental findById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Rental with id {0} not found", id)
                ));
    }

    @Override
    public Page<Rental> findPageClosedRentalsWithFilter(ClosedRentalFilterRequest request) {
        return rentalRepository.findAllClosedWithFilter(request);
    }

    @Override
    @Transactional
    public Rental create(Book book, User user, LocalDate rentDate, Integer rentDuration) {
        if (book.getAvailableCount() == 0) {
            throw new OutOfStockException("No available books");
        }
        if (book.getDeletedAt() != null) {
            throw new IllegalStateException("Can't rent deleted book");
        }
        Rental rental = new Rental();
        rental.setRentDate(rentDate);
        rental.setDuration(rentDuration);
        user.addRental(rental);
        book.addRental(rental);
        book.setAvailableCount(book.getAvailableCount() - 1);
        return rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public Rental update(Rental rental) {
        return rentalRepository.update(rental);
    }

    @Override
    @Transactional
    public void closeRental(Rental rental) {
        rental.setReturnDate(LocalDate.now());
        Book book = rental.getBook();
        book.setAvailableCount(book.getAvailableCount() + 1);
    }
}
