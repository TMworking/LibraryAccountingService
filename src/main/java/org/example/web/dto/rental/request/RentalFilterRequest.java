package org.example.web.dto.rental.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalFilterRequest {

    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private String sortBy = "rentDate";
    @Builder.Default
    private String sortDirection = "ASC";

    private Long userId;
    private Long bookId;
    private Integer minDuration;
    private Integer maxDuration;
    @Builder.Default
    private LocalDateTime rentDateFrom = LocalDateTime.now().minusMonths(3);
    @Builder.Default
    private LocalDateTime rentDateTo = LocalDateTime.now();
}
