package org.example.web.dto.book.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFilterRequest {

    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private String sortBy = "name";
    @Builder.Default
    private String sortDirection = "ASC";

    private String name;
    private String authorName;
    private List<Long> catalogIds;
    private Boolean isAvailable;
}
