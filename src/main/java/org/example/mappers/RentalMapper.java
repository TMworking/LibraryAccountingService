package org.example.mappers;

import org.example.domain.Rental;
import org.example.model.Page;
import org.example.web.dto.Meta;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.rental.response.RentalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    RentalResponse toResponse(Rental rental);

    default RentalPageResponse toPageResponse(Page<Rental> rentalPage) {
        return new RentalPageResponse(
                rentalPage.getContent().stream().map(this::toResponse).toList(),
                new Meta(rentalPage.getPageNumber(), rentalPage.getPageSize(), rentalPage.getTotalRecords())
        );
    }
}
