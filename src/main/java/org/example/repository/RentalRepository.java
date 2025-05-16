package org.example.repository;

import org.example.domain.Rental;
import org.example.model.Page;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.RentalFilterRequest;

import java.util.Optional;

public interface RentalRepository {
    Optional<Rental> findById(Long id);
    Rental update(Rental rental);
    Rental save(Rental rental);
    Page<Rental> findAllClosedWithFilter(ClosedRentalFilterRequest filterRequest);
    Page<Rental> findByUserIdWithFilter(Long id, RentalFilterRequest filterRequest);
    Page<Rental> findByBookIdWithFilter(Long id, RentalFilterRequest filterRequest);
    long countActiveRentalsByBookId(Long bookId);
}
