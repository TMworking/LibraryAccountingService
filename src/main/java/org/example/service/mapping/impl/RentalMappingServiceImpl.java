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
import org.springframework.stereotype.Service;

@Service
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
        return rentalMapper.toResponse(rentalService.create(book, user, request.getRentDate(), request.getDuration()));
    }

    @Override
    public void closeRental(Long id) {
        Rental rental = rentalService.findById(id);
        rentalService.closeRental(rental);
    }

    @Override
    public RentalResponse updateRental(Long id, RentalProlongationRequest request) {
        Rental rental = rentalService.findById(id);
        Integer currDuration = rental.getDuration();
        rental.setDuration(currDuration + request.getDays());
        return rentalMapper.toResponse(rentalService.update(rental));
    }
}
