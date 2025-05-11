package org.example.service.domain;

import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.domain.User;
import org.example.model.Page;
import org.example.web.dto.rental.request.RentalFilterRequest;

import java.time.LocalDate;

public interface RentalService {
    Rental findById(Long id);
    Page<Rental> findPageRentalsWithFilter(RentalFilterRequest request);
    Rental create(Book book, User user, LocalDate rentDate, Integer rentDuration);
    Rental update(Rental rental);
    void closeRental(Rental rental);
}
