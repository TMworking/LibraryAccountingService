package org.example.service.mapping;

import org.example.web.dto.book.request.AddBooksRequest;
import org.example.web.dto.catalog.request.CatalogChangeParentRequest;
import org.example.web.dto.catalog.request.CatalogCreateRequest;
import org.example.web.dto.catalog.request.CatalogUpdateRequest;
import org.example.web.dto.catalog.response.CatalogPageResponse;
import org.example.web.dto.catalog.response.CatalogResponse;

public interface CatalogMappingService {
    CatalogPageResponse getAllCatalogs();
    CatalogResponse createCatalog(CatalogCreateRequest request);
    CatalogResponse updateCatalog(Long id, CatalogUpdateRequest request);
    void addBooksToCatalog(Long id, AddBooksRequest request);
    CatalogResponse changeCatalogParent(Long id, CatalogChangeParentRequest request);
}
