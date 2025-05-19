package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Book;
import org.example.exception.BookDeletionException;
import org.example.model.Page;
import org.example.model.SortOption;
import org.example.repository.BookRepository;
import org.example.repository.RentalRepository;
import org.example.service.domain.impl.BookServiceImpl;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.book.request.BookFilterRequest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldFindByIdAndReturnBookWhenBookExists() {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();

        // When
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBook));
        Book actualBook = bookService.findById(1L);

        // Then
        assertThat(actualBook).isEqualTo(expectedBook);
        verify(bookRepository).findById(1L);
    }

    @Test
    void shouldFindByIdAndThrowEntityNotFoundExceptionWhenBookNotExists() {
        // When
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(999L));
        verify(bookRepository).findById(999L);
    }

    @Test
    void shouldCreateBookAndReturnSavedBook() {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();

        // When
        when(bookRepository.save(any(Book.class))).thenReturn(expectedBook);
        Book actualBook = bookService.createBook(expectedBook);

        // Then
        assertThat(actualBook).isEqualTo(expectedBook);
        verify(bookRepository).save(expectedBook);
    }

    @Test
    void shouldUpdateBookAndReturnUpdatedBook() {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();

        // When
        when(bookRepository.update(any(Book.class))).thenReturn(expectedBook);
        Book actualBook = bookService.updateBook(expectedBook);

        // Then
        assertThat(actualBook).isEqualTo(expectedBook);
        verify(bookRepository).update(expectedBook);
    }

    @Test
    void shouldDeleteBookWhenNoActiveRentals() {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();
        expectedBook.setDeletedAt(null);

        // When
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBook));
        when(rentalRepository.countActiveRentalsByBookId(1L)).thenReturn(0L);
        bookService.deleteBook(1L);

        // Then
        assertThat(expectedBook.getDeletedAt()).isNotNull();
        verify(bookRepository).findById(1L);
        verify(rentalRepository).countActiveRentalsByBookId(1L);
    }

    @Test
    void shouldNotDeleteBookAndThrowBookDeletionExceptionWhenActiveRentalsExist() {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();

        // When
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBook));
        when(rentalRepository.countActiveRentalsByBookId(1L)).thenReturn(1L);

        // Then
        assertThrows(BookDeletionException.class,
                () -> bookService.deleteBook(1L));
        verify(rentalRepository).countActiveRentalsByBookId(1L);
    }

    @Test
    void shouldGetAllBooksByIdsAndReturnBooksWhenBooksExist() {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();
        List<Long> ids = List.of(1L);

        // When
        when(bookRepository.findAllByIds(ids)).thenReturn(List.of(expectedBook));
        List<Book> actualResult = bookService.getAllBooksByIds(ids);

        // Then
        assertTrue(actualResult.contains(expectedBook));
        verify(bookRepository).findAllByIds(ids);
    }

    @Test
    void shouldNotGetAllBooksByIdsReturnEmptyListWhenBooksNotExist() {
        // When
        when(bookRepository.findAllByIds(anyList())).thenReturn(Collections.emptyList());
        List<Book> result = bookService.getAllBooksByIds(List.of(999L));

        // Then
        assertTrue(result.isEmpty());
        verify(bookRepository).findAllByIds(List.of(999L));
    }


    private static Stream<Arguments> provideBookFilterScenarios() {
        return Stream.of(
                Arguments.of(
                        BookFilterRequest.builder().build(),
                        1
                ),
                Arguments.of(
                        BookFilterRequest.builder()
                                .name("Spring in Action")
                                .build(),
                        1
                ),
                Arguments.of(
                        BookFilterRequest.builder()
                                .authorName("Leeroy")
                                .sortOptionList(List.of(new SortOption("name", "asc")))
                                .build(),
                        1
                ),
                Arguments.of(
                        BookFilterRequest.builder()
                                .page(1)
                                .size(5)
                                .build(),
                        0
                ),
                Arguments.of(
                        BookFilterRequest.builder()
                                .isAvailable(true)
                                .catalogIds(List.of(1L, 2L))
                                .sortOptionList(List.of(new SortOption("createdAt", "desc")))
                                .build(),
                        1
                ),
                Arguments.of(
                        BookFilterRequest.builder()
                                .name("Unknown")
                                .authorName("Unknown")
                                .build(),
                        0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideBookFilterScenarios")
    void shouldFindBooksWithFilterAndReturnPageOfBooks(
            BookFilterRequest request,
            int expectedContentSize
    ) {
        // Given
        Book expectedBook = TestObjectUtils.createTestBook();
        Page<Book> expectedPage = new Page<>(
                expectedContentSize > 0 ? List.of(expectedBook) : Collections.emptyList(),
                request.getPage(),
                request.getSize(),
                expectedContentSize
        );

        // When
        when(bookRepository.findAll(request)).thenReturn(expectedPage);
        Page<Book> actualPage = bookService.findBooksWithFilter(request);

        // Then
        assertNotNull(actualPage);
        assertEquals(actualPage.getContent(), expectedPage.getContent());
        assertEquals(actualPage.getPageNumber(), request.getPage());
        verify(bookRepository).findAll(request);
    }
}
