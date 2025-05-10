package org.example.repository;

import org.example.domain.Catalog;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {
    Optional<Catalog> findById(Long id);
    List<Catalog> findAllByIds(List<Long> ids);
    List<Catalog> findAllSubCatalogsRecursiveByIds(List<Long> rootIds);
    List<Catalog> findAllRootCatalogs();
    Catalog save(Catalog catalog);
    Catalog update(Catalog catalog);
}
