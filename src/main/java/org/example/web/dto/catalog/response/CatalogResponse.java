package org.example.web.dto.catalog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogResponse {
    private Long id;
    private String name;
    private Long parentId;
    private int booksCount;
}
