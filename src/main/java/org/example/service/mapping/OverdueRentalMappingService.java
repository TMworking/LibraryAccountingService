package org.example.service.mapping;

import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.example.web.dto.rental.response.OverdueRentalPageResponse;

public interface OverdueRentalMappingService {
    OverdueRentalPageResponse getAllOverdueRentals(OverdueRentalFilterRequest request);
}
