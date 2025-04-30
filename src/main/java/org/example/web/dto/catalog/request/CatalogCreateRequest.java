package org.example.web.dto.catalog.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogCreateRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    private Long parentId;
    @Builder.Default
    private List<Long> childrenIds = new ArrayList<>();
}
