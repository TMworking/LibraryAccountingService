package org.example.web.dto.catalog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogShortResponse {
    private Long id;
    private String name;
}
