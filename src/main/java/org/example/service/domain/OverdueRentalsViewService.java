package org.example.service.domain;

import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;

public interface OverdueRentalsViewService {
    void refreshView();
    Page<OverdueRentalView> getOverdueRentalsWithFilter(OverdueRentalFilterRequest request);
}
