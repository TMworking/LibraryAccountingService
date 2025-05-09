package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.domain.User;
import org.example.mappers.RentalMapper;
import org.example.service.domain.BookService;
import org.example.service.domain.RentalService;
import org.example.service.domain.UserService;
import org.example.service.mapping.RentalMappingService;
import org.example.web.dto.rental.request.RentalCreateRequest;
import org.example.web.dto.rental.request.RentalProlongationRequest;
import org.example.web.dto.rental.response.RentalResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RentalMappingServiceImpl implements RentalMappingService {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final BookService bookService;
    private final UserService userService;


    @Override
    public RentalResponse getRentalById(Long id) {
        return rentalMapper.toResponse(rentalService.findById(id));
    }

    @Override
    public RentalResponse createRental(RentalCreateRequest request) {
        User user = userService.findById(request.getUserId());
        Book book = bookService.findById(request.getBookId());

        Rental rental = new Rental();
        rental.setRentDate(request.getRentDate());
        rental.setDuration(request.getDuration());
        user.addRental(rental);
        book.addRental(rental);

        return rentalMapper.toResponse(rentalService.save(rental));
    }

    @Override
    public void closeRental(Long id) {
        Rental rental = rentalService.findById(id);
        rental.setReturnDate(LocalDate.now());
        rentalService.update(rental);
    }

    @Override
    public RentalResponse updateRental(Long id, RentalProlongationRequest request) {
        Rental rental = rentalService.findById(id);
        Integer currDuration = rental.getDuration();
        rental.setDuration(currDuration + request.getDays());
        return rentalMapper.toResponse(rentalService.update(rental));
    }
}
