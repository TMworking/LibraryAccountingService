package org.example.web.dto.catalog.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogChangeParentRequest {
    @NotNull
    private Long parentId;
}
