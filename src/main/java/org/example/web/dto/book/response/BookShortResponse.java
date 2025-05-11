package org.example.web.dto.book.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.web.dto.author.response.AuthorResponse;
import org.example.web.dto.catalog.response.CatalogShortResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookShortResponse {
    private Long id;
    private String name;
    private Integer availableCount;
    private List<AuthorResponse> authors;
    private List<CatalogShortResponse> catalogs;
}
