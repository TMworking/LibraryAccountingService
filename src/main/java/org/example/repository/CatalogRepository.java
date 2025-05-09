package org.example.repository;

import org.example.domain.Catalog;

import java.util.List;

public interface CatalogRepository {
    List<Catalog> findAllByIds(List<Long> ids);
}
