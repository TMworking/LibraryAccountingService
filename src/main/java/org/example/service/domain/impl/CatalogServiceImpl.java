package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Book;
import org.example.domain.Catalog;
import org.example.exception.NotFoundException;
import org.example.repository.CatalogRepository;
import org.example.service.domain.BookService;
import org.example.service.domain.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final BookService bookService;

    @Override
    public Catalog findById(Long id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                MessageFormat.format("Catalog with id {0} not found", id)
        ));
    }

    @Override
    public List<Catalog> getAllByIds(List<Long> ids) {
        return catalogRepository.findAllByIds(ids);
    }

    @Override
    public List<Long> findAllSubCatalogIds(List<Long> parentIds) {
        List<Catalog> catalogs = catalogRepository.findAllSubCatalogsRecursiveByIds(parentIds);
        return catalogs.stream()
                .map(Catalog::getId)
                .toList();
    }

    @Override
    public List<Catalog> findAll() {
        return catalogRepository.findAllRootCatalogs();
    }

    @Override
    @Transactional
    public Catalog create(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    @Transactional
    public Catalog update(Catalog catalog) {
        return catalogRepository.update(catalog);
    }

    @Override
    @Transactional
    public Catalog addBooksToCatalog(Long id, List<Long> bookIds) {
        List<Book> books = bookService.findAllBooksByIds(bookIds);
        Catalog catalog = findById(id);
        catalog.addBooks(books);
        return catalog;
    }

    @Override
    @Transactional
    public Catalog changeCatalogParent(Long catalogId, Long newParentId) {
        if (Objects.equals(catalogId, newParentId)) {
            throw new IllegalStateException("Can't make catalog parent for himself");
        }

        Catalog catalog = findById(catalogId);
        Catalog oldParent = catalog.getParent();

        if (oldParent.getId().equals(newParentId)) {
            return catalog;
        }

        oldParent.removeChild(catalog);

        if (newParentId != null) {
            Catalog newParent = findById(newParentId);
            newParent.addChild(catalog);
        }

        return catalog;
    }
}
