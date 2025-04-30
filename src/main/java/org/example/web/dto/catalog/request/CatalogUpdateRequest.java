package org.example.web.dto.catalog.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogUpdateRequest {
    @Size(min = 3, max = 50)
    private String name;
    private Long parentId;
}
