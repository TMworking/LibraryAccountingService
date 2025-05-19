package org.example.service.domain;

import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.domain.User;
import org.example.model.Page;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.RentalFilterRequest;

import java.time.LocalDate;

public interface RentalService {
    Rental findById(Long id);
    Page<Rental> findPageClosedRentalsWithFilter(ClosedRentalFilterRequest request);
    Rental create(Book book, User user, LocalDate rentDate, Integer rentDuration);
    Rental update(Rental rental);
    void closeRental(Rental rental);
    Page<Rental> getBookRentals(Long id, RentalFilterRequest filterRequest);
    Page<Rental> getUserRentals(Long id, RentalFilterRequest filterRequest);
}
