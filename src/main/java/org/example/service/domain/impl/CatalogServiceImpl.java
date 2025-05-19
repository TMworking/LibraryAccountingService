package org.example.service.domain.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Book;
import org.example.domain.Catalog;
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
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final BookService bookService;

    @Override
    public Catalog findById(Long id) {
        log.debug("Searching catalog by ID: {}", id);
        Catalog catalog = catalogRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Catalog with id {} not found", id);
                    return new EntityNotFoundException(
                            MessageFormat.format("Catalog with id {0} not found", id)
                    );
                });
        log.debug("Found catalog: ID = {}, name = {}", id, catalog.getName());
        return catalog;
    }

    @Override
    public List<Catalog> getAllByIds(List<Long> ids) {
        log.debug("Fetching multiple catalogs by IDs: {}", ids);
        List<Catalog> catalogs = catalogRepository.findAllByIds(ids);
        log.debug("Found {} catalogs out of requested {}", catalogs.size(), ids.size());
        return catalogs;
    }

    @Override
    public List<Long> findAllSubCatalogIds(List<Long> parentIds) {
        log.debug("Fetching all child catalogs by parent IDs: {}", parentIds);
        List<Catalog> catalogs = catalogRepository.findAllSubCatalogsRecursiveByIds(parentIds);
        log.debug("Found {} catalogs", catalogs.size());
        return catalogs.stream()
                .map(Catalog::getId)
                .toList();
    }

    @Override
    public List<Catalog> findAllRoots() {
        log.debug("Fetching all root catalogs");
        List<Catalog> catalogs = catalogRepository.findAllRootCatalogs();
        log.debug("Found {} root catalogs", catalogs.size());
        return catalogs;
    }

    @Override
    @Transactional
    public Catalog create(Catalog catalog) {
        log.debug("Creating new catalog: Title={}", catalog.getName());
        Catalog savedCatalog = catalogRepository.save(catalog);
        log.debug("Catalog created successfully: {}", savedCatalog.getName());
        return savedCatalog;
    }

    @Override
    @Transactional
    public Catalog update(Catalog catalog) {
        log.debug("Updating catalog: ID={}", catalog.getId());
        Catalog updatedCatalog = catalogRepository.update(catalog);
        log.debug("Catalog updated successfully: {}", catalog);
        return updatedCatalog;
    }

    @Override
    @Transactional
    public Catalog addBooksToCatalog(Long id, List<Long> bookIds) {
        log.debug("Adding {} books to catalog: ID={}", bookIds.size(), id);
        List<Book> books = bookService.getAllBooksByIds(bookIds);
        log.debug("Found {} books to add", books.size());
        Catalog catalog = findById(id);
        catalog.addBooks(books);
        log.debug("Successfully added books to catalog: ID={}", id);
        return catalog;
    }

    @Override
    @Transactional
    public Catalog changeCatalogParent(Long catalogId, Long newParentId) {
        log.debug("Changing parent for catalog: CatalogID={}, NewParentID={}", catalogId, newParentId);
        if (Objects.equals(catalogId, newParentId)) {
            log.error("Can't make catalog parent for himself: CatalogID={}", catalogId);
            throw new IllegalStateException("Can't make catalog parent for himself");
        }

        Catalog catalog = findById(catalogId);
        Catalog oldParent = catalog.getParent();

        if (oldParent != null && oldParent.getId().equals(newParentId)) {
            log.debug("Catalog already has the same parent: CatalogID={}", catalogId);
            return catalog;
        }

        if (oldParent != null) {
            log.debug("Removing from old parent: OldParentID={}", oldParent.getId());
            oldParent.removeChild(catalog);
        }

        if (newParentId != null) {
            Catalog newParent = findById(newParentId);
            log.debug("Adding to new parent: NewParentID={}", newParentId);
            newParent.addChild(catalog);
        } else {
            log.debug("Making catalog root: CatalogId={}", catalogId);
        }

        log.debug("Successfully changed parent for catalog: ID={}", catalogId);
        return catalog;
    }
}
