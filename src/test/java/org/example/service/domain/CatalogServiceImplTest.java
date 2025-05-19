package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Book;
import org.example.domain.Catalog;
import org.example.repository.CatalogRepository;
import org.example.service.domain.impl.CatalogServiceImpl;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceImplTest {

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CatalogServiceImpl catalogService;

    @Test
    void shouldFindByIdAndReturnCatalogWhenCatalogExists() {
        // Given
        Catalog expectedCatalog = TestObjectUtils.createTestCatalog();

        // When
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(expectedCatalog));
        Catalog actualCatalog = catalogService.findById(1L);

        // Then
        assertThat(actualCatalog).isEqualTo(expectedCatalog);
        verify(catalogRepository).findById(1L);
    }

    @Test
    void shouldFindByIdAndThrowEntityNotFoundExceptionWhenCatalogNotExists() {
        // When
        when(catalogRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> catalogService.findById(999L));
        verify(catalogRepository).findById(999L);
    }

    @Test
    void shouldGetAllByIdsAndReturnCatalogsWhenCatalogsExist() {
        // Given
        Catalog expectedCatalog = TestObjectUtils.createTestCatalog();
        List<Long> ids = List.of(1L);

        // When
        when(catalogRepository.findAllByIds(ids)).thenReturn(List.of(expectedCatalog));
        List<Catalog> actualResult = catalogService.getAllByIds(ids);

        // Then
        assertTrue(actualResult.contains(expectedCatalog));
        verify(catalogRepository).findAllByIds(ids);
    }

    @Test
    void shouldFindAllSubCatalogIdsAndReturnIdsWhenSubCatalogsExist() {
        // Given
        Catalog parentCatalog = TestObjectUtils.createTestCatalog();
        Catalog childCatalog = TestObjectUtils.createTestCatalog();
        parentCatalog.addChild(childCatalog);
        List<Long> parentIds = List.of(1L);

        // When
        when(catalogRepository.findAllSubCatalogsRecursiveByIds(parentIds))
                .thenReturn(List.of(parentCatalog, childCatalog));
        List<Long> result = catalogService.findAllSubCatalogIds(parentIds);

        // Then
        assertTrue(result.contains(childCatalog.getId()));
        verify(catalogRepository).findAllSubCatalogsRecursiveByIds(parentIds);
    }

    @Test
    void shouldFindAllRootsAndReturnRootCatalogsWhenTheyExist() {
        // Given
        Catalog expectedCatalog = TestObjectUtils.createTestCatalog();
        expectedCatalog.setParent(null);

        // When
        when(catalogRepository.findAllRootCatalogs()).thenReturn(List.of(expectedCatalog));
        List<Catalog> actualCatalogs = catalogService.findAllRoots();

        // Then
        assertTrue(actualCatalogs.contains(expectedCatalog));
        verify(catalogRepository).findAllRootCatalogs();
    }

    @Test
    void shouldCreateAndReturnSavedCatalog() {
        // Given
        Catalog expectedCatalog = TestObjectUtils.createTestCatalog();

        // When
        when(catalogRepository.save(any(Catalog.class))).thenReturn(expectedCatalog);
        Catalog actualCatalog = catalogService.create(expectedCatalog);

        // Then
        assertThat(actualCatalog).isEqualTo(expectedCatalog);
        verify(catalogRepository).save(expectedCatalog);
    }

    @Test
    void shouldUpdateAndReturnUpdatedCatalog() {
        // Given
        Catalog expectedCatalog = TestObjectUtils.createTestCatalog();

        // When
        when(catalogRepository.update(any(Catalog.class))).thenReturn(expectedCatalog);
        Catalog actualCatalog = catalogService.update(expectedCatalog);

        // Then
        assertThat(actualCatalog).isEqualTo(expectedCatalog);
        verify(catalogRepository).update(expectedCatalog);
    }

    @Test
    void shouldAddBooksToCatalogWhenCatalogAndBooksExist() {
        // Given
        Catalog expectedCatalog = TestObjectUtils.createTestCatalog();
        Book expectedBook = TestObjectUtils.createTestBook();
        List<Long> bookIds = List.of(1L);

        // When
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(expectedCatalog));
        when(bookService.getAllBooksByIds(bookIds)).thenReturn(List.of(expectedBook));
        Catalog actualCatalog = catalogService.addBooksToCatalog(1L, bookIds);

        // Then
        assertTrue(actualCatalog.getBooks().contains(expectedBook));
        verify(catalogRepository).findById(1L);
        verify(bookService).getAllBooksByIds(bookIds);
    }

    @Test
    void shouldNotAddBooksToCatalogAndThrowNotFoundExceptionWhenCatalogNotExists() {
        // Given
        List<Long> bookIds = List.of(1L);

        // When
        when(catalogRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> catalogService.addBooksToCatalog(999L, bookIds));
        verify(catalogRepository).findById(999L);
    }

    @Test
    void shouldChangeCatalogParentWhenNewParentIsDifferent() {
        // Given
        Catalog childCatalog = TestObjectUtils.createTestCatalog();
        Catalog parentCatalog = TestObjectUtils.createTestCatalog();
        parentCatalog.setId(2L);

        // When
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(childCatalog));
        when(catalogRepository.findById(2L)).thenReturn(Optional.of(parentCatalog));
        Catalog result = catalogService.changeCatalogParent(1L, 2L);

        // Then
        assertEquals(result.getParent(), parentCatalog);
        verify(catalogRepository, times(2)).findById(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenTryingToMakeCatalogParentForItself() {
        // Given

        // When

        // Then
        assertThrows(IllegalStateException.class,
                () -> catalogService.changeCatalogParent(1L, 1L));
    }

    @Test
    void shouldNotChangeParentWhenNewParentIsSameAsCurrent() {
        // Given
        Catalog childCatalog = TestObjectUtils.createTestCatalog();
        Catalog parentCatalog = TestObjectUtils.createTestCatalog();
        parentCatalog.setId(2L);
        parentCatalog.addChild(childCatalog);

        // When
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(childCatalog));
        Catalog actualCatalog = catalogService.changeCatalogParent(1L, 2L);

        // Then
        assertEquals(actualCatalog.getParent(), parentCatalog);
        verify(catalogRepository).findById(1L);
        verify(catalogRepository, never()).findById(2L);
    }

    @Test
    void shouldMakeCatalogRootWhenNewParentIsNull() {
        // Given
        Catalog childCatalog = TestObjectUtils.createTestCatalog();
        Catalog parentCatalog = TestObjectUtils.createTestCatalog();
        parentCatalog.addChild(parentCatalog);

        // When
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(childCatalog));
        Catalog result = catalogService.changeCatalogParent(1L, null);

        // Then
        assertThat(result.getParent()).isNull();
        assertFalse(parentCatalog.getChildren().contains(childCatalog));
        verify(catalogRepository).findById(1L);
    }
}
