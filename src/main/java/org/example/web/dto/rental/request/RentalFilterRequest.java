package org.example.web.dto.rental.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.web.dto.common.SortOption;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    private List<SortOption> sortOptions = List.of(new SortOption("id", "asc"));

    private Long userId;
    private Long bookId;
    private Integer minDuration;
    private Integer maxDuration;
    @Builder.Default
    private LocalDateTime rentDateFrom = LocalDateTime.now().minusMonths(3);
    @Builder.Default
    private LocalDateTime rentDateTo = LocalDateTime.now();
}
