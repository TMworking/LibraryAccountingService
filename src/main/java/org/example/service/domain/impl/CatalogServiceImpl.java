package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Catalog;
import org.example.repository.CatalogRepository;
import org.example.service.domain.CatalogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public List<Catalog> getAllByIds(List<Long> ids) {
        return catalogRepository.findAllByIds(ids);
    }
}
