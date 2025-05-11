package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.mapping.CatalogMappingService;
import org.example.web.dto.book.request.AddBooksRequest;
import org.example.web.dto.catalog.request.CatalogChangeParentRequest;
import org.example.web.dto.catalog.request.CatalogCreateRequest;
import org.example.web.dto.catalog.request.CatalogUpdateRequest;
import org.example.web.dto.catalog.response.CatalogPageResponse;
import org.example.web.dto.catalog.response.CatalogResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalogs")
@Tag(name = "Catalog")
public class CatalogController {

    private final CatalogMappingService catalogMappingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<CatalogPageResponse> getAllCatalogs() {
        return ResponseEntity.ok().body(catalogMappingService.getAllCatalogs());
    }

    @PostMapping
    public ResponseEntity<CatalogResponse> createCatalog(@Valid @RequestBody CatalogCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogMappingService.createCatalog(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CatalogResponse> updateCatalog(@PathVariable("id") Long id, @Valid @RequestBody CatalogUpdateRequest request) {
        return ResponseEntity.ok().body(catalogMappingService.updateCatalog(id, request));
    }

    @PatchMapping("/{id}/parent")
    public ResponseEntity<CatalogResponse> changeCatalogParent(@PathVariable("id") Long id, @Valid @RequestBody CatalogChangeParentRequest request) {
        return ResponseEntity.ok().body(catalogMappingService.changeCatalogParent(id, request));
    }

    @PatchMapping("/{id}/books")
    public ResponseEntity<Void> addBooksToCatalog(@PathVariable("id") Long id, @Valid @RequestBody AddBooksRequest request) {
        catalogMappingService.addBooksToCatalog(id, request);
        return ResponseEntity.ok().body(null);
    }
}
