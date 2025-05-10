package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.mappers.OverdueRentalMapper;
import org.example.service.domain.OverdueRentalsViewService;
import org.example.service.mapping.OverdueRentalMappingService;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.example.web.dto.rental.response.OverdueRentalPageResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OverdueRentalsMappingServiceImpl implements OverdueRentalMappingService {

    private final OverdueRentalMapper overdueRentalMapper;
    private final OverdueRentalsViewService overdueRentalsViewService;

    @Override
    public OverdueRentalPageResponse getAllOverdueRentals(OverdueRentalFilterRequest request) {
        return overdueRentalMapper.toOverduePageResponse(overdueRentalsViewService.getOverdueRentalsWithFilter(request));
    }
}
