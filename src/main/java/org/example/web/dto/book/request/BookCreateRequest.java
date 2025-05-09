package org.example.web.dto.book.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @Min(value = 0)
    @NotNull
    private Integer availableCount;

    @Size(max = 1000)
    private String description;

    private LocalDate publishingDate;

    @Builder.Default
    private List<Long> authorIds = new ArrayList<>();

    @Builder.Default
    private List<Long> catalogIds = new ArrayList<>();
}
