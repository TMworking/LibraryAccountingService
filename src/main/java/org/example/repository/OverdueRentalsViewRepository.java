package org.example.repository;

import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;

public interface OverdueRentalsViewRepository {
    void refreshOverdueRentalsView();
    Page<OverdueRentalView> findAllWithFilter(OverdueRentalFilterRequest filterRequest);
}
