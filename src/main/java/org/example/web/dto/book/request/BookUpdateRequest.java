package org.example.web.dto.book.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {
    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 1000)
    private String description;

    @Min(value = 0)
    private Integer availableCount;

    private List<Long> authorIds;

    private List<Long> catalogIds;
}
