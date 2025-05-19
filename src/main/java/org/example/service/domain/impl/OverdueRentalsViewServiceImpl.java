package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.repository.OverdueRentalsViewRepository;
import org.example.service.domain.OverdueRentalsViewService;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OverdueRentalsViewServiceImpl implements OverdueRentalsViewService {

    private final OverdueRentalsViewRepository overdueRentalsViewRepository;

    @Override
    public void refreshView() {
        log.debug("Refreshing overdue rentals view");
        overdueRentalsViewRepository.refreshOverdueRentalsView();
        log.info("Successfully refreshed overdue rentals view");
    }

    @Override
    public Page<OverdueRentalView> getOverdueRentalsWithFilter(OverdueRentalFilterRequest request) {
        log.debug("Fetching overdue rentals with filter: {}", request);
        Page<OverdueRentalView> page = overdueRentalsViewRepository.findAllWithFilter(request);
        log.debug("Retrieved {} overdue rentals (page {})",
                page.getContent().size(),
                page.getPageNumber());
        return page;
    }
}
