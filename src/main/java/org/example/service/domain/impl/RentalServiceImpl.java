package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Rental;
import org.example.exception.NotFoundException;
import org.example.model.Page;
import org.example.repository.RentalRepository;
import org.example.service.domain.RentalService;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

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
    public Page<Rental> findPageRentalsWithFilter(RentalFilterRequest request) {
        return rentalRepository.findAllWithFilter(request);
    }

    @Override
    @Transactional
    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public Rental update(Rental rental) {
        return rentalRepository.update(rental);
    }
}
