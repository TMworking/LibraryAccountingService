package org.example.mappers;

import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.web.dto.rental.response.OverdueRentalPageResponse;
import org.example.web.dto.rental.response.OverdueRentalResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OverdueRentalMapper {

    OverdueRentalResponse toResponse(OverdueRentalView overdueRentalView);

    default OverdueRentalPageResponse toOverduePageResponse(Page<OverdueRentalView> page) {
        return new OverdueRentalPageResponse(
                page.getContent().stream().map(this::toResponse).toList(),
                page.getPageNumber(),
                page.getPageSize(),
                page.getTotalRecords()
        );
    }
}
