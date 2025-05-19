package org.example.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.mapping.CatalogMappingService;
import org.example.web.dto.book.request.AddBooksRequest;
import org.example.web.dto.catalog.request.CatalogChangeParentRequest;
import org.example.web.dto.catalog.request.CatalogCreateRequest;
import org.example.web.dto.catalog.request.CatalogUpdateRequest;
import org.example.web.dto.catalog.response.CatalogPageResponse;
import org.example.web.dto.catalog.response.CatalogResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogControllerTest {

    @Mock
    private CatalogMappingService catalogMappingService;

    @InjectMocks
    private CatalogController catalogController;

    @Test
    void shouldGetAllCatalogsAndReturnResponse() {
        // Given
        CatalogPageResponse expected = new CatalogPageResponse();

        // When
        when(catalogMappingService.getAllCatalogs()).thenReturn(expected);
        ResponseEntity<CatalogPageResponse> actualResponse = catalogController.getAllCatalogs();

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(catalogMappingService).getAllCatalogs();
    }

    @Test
    void shouldCreateCatalogAndReturnCreatedResponse() {
        // Given
        CatalogCreateRequest request = new CatalogCreateRequest();
        CatalogResponse expected = new CatalogResponse();

        // When
        when(catalogMappingService.createCatalog(request)).thenReturn(expected);
        ResponseEntity<CatalogResponse> actualResponse = catalogController.createCatalog(request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(catalogMappingService).createCatalog(request);
    }

    @Test
    void shouldUpdateCatalogAndReturnUpdatedResponse() {
        // Given
        Long id = 1L;
        CatalogUpdateRequest request = new CatalogUpdateRequest();
        CatalogResponse expected = new CatalogResponse();

        // When
        when(catalogMappingService.updateCatalog(id, request)).thenReturn(expected);
        ResponseEntity<CatalogResponse> actualResponse = catalogController.updateCatalog(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(catalogMappingService).updateCatalog(id, request);
    }

    @Test
    void shouldChangeCatalogParentAndReturnUpdatedResponse() {
        // Given
        Long id = 1L;
        CatalogChangeParentRequest request = new CatalogChangeParentRequest();
        CatalogResponse expected = new CatalogResponse();

        // When
        when(catalogMappingService.changeCatalogParent(id, request)).thenReturn(expected);
        ResponseEntity<CatalogResponse> actualResponse = catalogController.changeCatalogParent(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(catalogMappingService).changeCatalogParent(id, request);
    }

    @Test
    void shouldAddBooksToCatalogAndReturnOkResponse() {
        // given
        Long id = 1L;
        AddBooksRequest request = new AddBooksRequest();

        // when
        doNothing().when(catalogMappingService).addBooksToCatalog(id, request);
        ResponseEntity<Void> response = catalogController.addBooksToCatalog(id, request);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(catalogMappingService).addBooksToCatalog(id, request);
    }

    @Test
    void shouldThrowExceptionWhenCreateCatalogFails() {
        // Given
        CatalogCreateRequest request = new CatalogCreateRequest();

        // When
        when(catalogMappingService.createCatalog(request)).thenThrow(new RuntimeException("Create failed"));

        // Then
        assertThrows(RuntimeException.class, () -> catalogController.createCatalog(request));
        verify(catalogMappingService).createCatalog(request);
    }

    @Test
    void shouldThrowExceptionWhenUpdateCatalogFails() {
        // Given
        Long id = 999L;
        CatalogUpdateRequest request = new CatalogUpdateRequest();

        // When
        when(catalogMappingService.updateCatalog(id, request)).thenThrow(new EntityNotFoundException("Update failed"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> catalogController.updateCatalog(id, request));
        verify(catalogMappingService).updateCatalog(id, request);
    }

    @Test
    void shouldThrowExceptionWhenChangeCatalogParentFails() {
        // Given
        Long id = 999L;
        CatalogChangeParentRequest request = new CatalogChangeParentRequest();

        // When
        when(catalogMappingService.changeCatalogParent(id, request)).thenThrow(new RuntimeException("Parent change failed"));

        // Then
        assertThrows(RuntimeException.class, () -> catalogController.changeCatalogParent(id, request));
        verify(catalogMappingService).changeCatalogParent(id, request);
    }

    @Test
    void shouldThrowExceptionWhenAddBooksToCatalogFails() {
        // Given
        Long id = 999L;
        AddBooksRequest request = new AddBooksRequest();

        // When
        doThrow(new EntityNotFoundException("Catalog not found")).when(catalogMappingService).addBooksToCatalog(id, request);

        // Then
        assertThrows(EntityNotFoundException.class, () -> catalogController.addBooksToCatalog(id, request));
        verify(catalogMappingService).addBooksToCatalog(id, request);
    }
}
