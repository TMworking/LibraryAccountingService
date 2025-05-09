package org.example.service.domain;

import org.example.domain.Rental;
import org.example.model.Page;
import org.example.web.dto.rental.request.RentalFilterRequest;

public interface RentalService {
    Rental findById(Long id);
    Page<Rental> findPageRentalsWithFilter(RentalFilterRequest request);
    Rental save(Rental rental);
    Rental update(Rental rental);
}
