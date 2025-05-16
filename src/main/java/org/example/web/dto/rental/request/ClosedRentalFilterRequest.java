package org.example.web.dto.rental.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.SortOption;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosedRentalFilterRequest {
    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private List<SortOption> sortOptions = List.of(new SortOption("id", "asc"));

    @Builder.Default
    private LocalDate rentDateFrom = LocalDate.now().minusMonths(3);
    @Builder.Default
    private LocalDate rentDateTo = LocalDate.now();
}
