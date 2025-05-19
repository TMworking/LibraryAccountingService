package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.model.Page;
import org.example.model.SortOption;
import org.example.repository.AuthorRepository;
import org.example.service.domain.impl.AuthorServiceImpl;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void shouldFindByIdAndReturnAuthorWhenAuthorExists() {
        // Given
        Author expectedAuthor = TestObjectUtils.createTestAuthor();

        // When
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        Author actualAuthor = authorService.findById(1L);

        // Then
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
        verify(authorRepository).findById(1L);
    }

    @Test
    void shouldFindByIdAndThrowEntityNotFoundExceptionWhenAuthorNotExists() {
        // Given

        // When
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> authorService.findById(999L));
        verify(authorRepository).findById(999L);
    }

    @Test
    void shouldSaveNewAuthorAndReturnSavedAuthor() {
        // Given
        Author expectedAuthor = TestObjectUtils.createTestAuthor();

        // When
        when(authorRepository.save(any(Author.class))).thenReturn(expectedAuthor);
        Author actualAuthor = authorService.saveAuthor(expectedAuthor);

        // Then
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
        verify(authorRepository).save(expectedAuthor);
    }

    @Test
    void shouldUpdateAndReturnAuthor() {
        // Given
        Author expectedAuthor = TestObjectUtils.createTestAuthor();

        // When
        when(authorRepository.update(any(Author.class))).thenReturn(expectedAuthor);
        Author actualAuthor = authorService.updateAuthor(expectedAuthor);

        // Then
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
        verify(authorRepository).update(expectedAuthor);
    }

    @Test
    void shouldAddBooksToAuthorWhenAuthorAndBooksExist() {
        // Given
        Author expectedAuthor = TestObjectUtils.createTestAuthor();
        Book expectedBook = TestObjectUtils.createTestBook();
        List<Long> bookIds = List.of(1L);

        // When
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(bookService.getAllBooksByIds(bookIds)).thenReturn(List.of(expectedBook));
        Author acualAuthor = authorService.addBooksToAuthor(1L, bookIds);

        // Then
        assertTrue(acualAuthor.getBooks().contains(expectedBook));
        verify(authorRepository).findById(1L);
        verify(bookService).getAllBooksByIds(bookIds);
    }

    @Test
    void shouldNotAddBooksToAuthorAndThrowNotFoundExceptionWhenAuthorNotExists() {
        // Given
        List<Long> bookIds = List.of(1L);

        // When
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> authorService.addBooksToAuthor(999L, bookIds));
        verify(authorRepository).findById(999L);
    }

    @Test
    void shouldGetAllByIdsAndReturnAuthorsWhenAuthorsExist() {
        // Given
        Author expectedAuthor = TestObjectUtils.createTestAuthor();
        List<Long> ids = List.of(1L);

        // When
        when(authorRepository.findAllByIds(ids)).thenReturn(List.of(expectedAuthor));
        List<Author> actualResult = authorService.getAllByIds(ids);

        // Then
        assertTrue(actualResult.contains(expectedAuthor));
        verify(authorRepository).findAllByIds(ids);
    }

    @Test
    void shouldNotGetAllByIdsReturnEmptyListWhenAuthorsNotExist() {
        // Given

        // When
        when(authorRepository.findAllByIds(anyList())).thenReturn(Collections.emptyList());
        List<Author> result = authorService.getAllByIds(List.of(999L));

        // Then
        assertTrue(result.isEmpty());
        verify(authorRepository).findAllByIds(List.of(999L));
    }

    private static Stream<Arguments> provideAuthorSortRequests() {
        return Stream.of(
                Arguments.of(new AuthorSortRequest(0, 10, List.of(new SortOption("name", "asc"))), 0, 1),
                Arguments.of(new AuthorSortRequest(1, 5, List.of(new SortOption("name", "desc"))), 1, 1),
                Arguments.of(new AuthorSortRequest(0, 20, Collections.emptyList()), 0, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAuthorSortRequests")
    void shouldFindPageAuthorsWithSortAndReturnPageOfAuthors(AuthorSortRequest request, int expectedPageNumber, int expectedRecordsCount) {
        // Given
        Author expectedAuthor = TestObjectUtils.createTestAuthor();
        Page<Author> expectedPage = new Page<>(List.of(expectedAuthor), expectedPageNumber, 10, expectedRecordsCount);

        // When
        when(authorRepository.findAll(request)).thenReturn(expectedPage);
        Page<Author> actualPage = authorService.findPageAuthorsWithSort(request);

        // Then
        assertNotNull(actualPage);
        assertThat(actualPage.getPageNumber()).isEqualTo(expectedPageNumber);
        assertThat(actualPage.getTotalRecords()).isEqualTo(expectedRecordsCount);
        verify(authorRepository).findAll(request);
    }
}
