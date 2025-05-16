package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.repository.OverdueRentalsViewRepository;
import org.example.service.domain.OverdueRentalsViewService;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OverdueRentalsViewServiceImpl implements OverdueRentalsViewService {

    private final OverdueRentalsViewRepository overdueRentalsViewRepository;

    @Override
    public void refreshView() {
        overdueRentalsViewRepository.refreshOverdueRentalsView();
    }

    @Override
    public Page<OverdueRentalView> getOverdueRentalsWithFilter(OverdueRentalFilterRequest request) {
        return overdueRentalsViewRepository.findAllWithFilter(request);
    }
}
