package org.example.service.domain;

import org.example.domain.Catalog;

import java.util.List;

public interface CatalogService {
    List<Catalog> getAllByIds(List<Long> ids);
}
