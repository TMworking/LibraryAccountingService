package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Catalog;
import org.example.mappers.CatalogMapper;
import org.example.service.domain.CatalogService;
import org.example.service.mapping.CatalogMappingService;
import org.example.web.dto.book.request.AddBooksRequest;
import org.example.web.dto.catalog.request.CatalogChangeParentRequest;
import org.example.web.dto.catalog.request.CatalogCreateRequest;
import org.example.web.dto.catalog.request.CatalogUpdateRequest;
import org.example.web.dto.catalog.response.CatalogPageResponse;
import org.example.web.dto.catalog.response.CatalogResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogMappingServiceImpl implements CatalogMappingService {

    private final CatalogMapper catalogMapper;
    private final CatalogService catalogService;

    @Override
    public CatalogPageResponse getAllCatalogs() {
        return catalogMapper.toPageResponse(catalogMapper.toResponseList(catalogService.findAll()));
    }

    @Override
    public CatalogResponse createCatalog(CatalogCreateRequest request) {
        Catalog catalog = catalogMapper.toCatalog(request);
        if (request.getParentId() != null) {
            Catalog parent = catalogService.findById(request.getParentId());
            parent.addChild(catalog);
        }
        return catalogMapper.toResponse(catalogService.create(catalog));
    }

    @Override
    public CatalogResponse updateCatalog(Long id, CatalogUpdateRequest request) {
        Catalog catalog = catalogService.findById(id);
        catalogMapper.updateFromRequest(catalog, request);
        Catalog updateCatalog = catalogService.update(catalog);
        return catalogMapper.toResponse(updateCatalog);
    }

    @Override
    public void addBooksToCatalog(Long id, AddBooksRequest request) {
        catalogMapper.toResponse(catalogService.addBooksToCatalog(id, request.getBooksIds()));
    }

    @Override
    public CatalogResponse changeCatalogParent(Long id, CatalogChangeParentRequest request) {
        return catalogMapper.toResponse(catalogService.changeCatalogParent(id, request.getParentId()));
    }
}
