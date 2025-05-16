package org.example.service.mapping;

import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.RentalCreateRequest;
import org.example.web.dto.rental.request.RentalProlongationRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.rental.response.RentalResponse;

public interface RentalMappingService {
    RentalResponse getRentalById(Long id);
    RentalResponse createRental(RentalCreateRequest request);
    void closeRental(Long id);
    RentalResponse updateRental(Long id, RentalProlongationRequest request);
    RentalPageResponse getAllClosedRentalsWithFilter(ClosedRentalFilterRequest request);
}
