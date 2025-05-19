package org.example.service.domain;

import org.example.domain.Catalog;

import java.util.List;

public interface CatalogService {
    Catalog findById(Long id);
    List<Catalog> getAllByIds(List<Long> ids);
    List<Long> findAllSubCatalogIds(List<Long> parentIds);
    List<Catalog> findAllRoots();
    Catalog create(Catalog catalog);
    Catalog update(Catalog catalog);
    Catalog addBooksToCatalog(Long id, List<Long> bookIds);
    Catalog changeCatalogParent(Long catalogId, Long newParentId);
}
