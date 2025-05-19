package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.domain.User;
import org.example.exception.OutOfStockException;
import org.example.model.Page;
import org.example.repository.RentalRepository;
import org.example.service.domain.impl.RentalServiceImpl;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RentalServiceImplTest {
    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    void shouldFindByIdAndReturnRentalWhenRentalExists() {
        // Given

        Rental expectedRental = TestObjectUtils.createTestRental();

        // When
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(expectedRental));
        Rental actualRental = rentalService.findById(1L);

        // Then
        assertThat(actualRental).isEqualTo(expectedRental);
        verify(rentalRepository).findById(1L);
    }

    @Test
    void shouldFindByIdAndThrowEntityNotFoundExceptionWhenRentalNotExists() {
        // When
        when(rentalRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> rentalService.findById(999L));
        verify(rentalRepository).findById(999L);
    }

    private static Stream<Arguments> provideClosedRentalFilterRequests() {
        return Stream.of(
                Arguments.of(
                        ClosedRentalFilterRequest.builder().build(),
                        1
                ),
                Arguments.of(
                        ClosedRentalFilterRequest.builder()
                                .rentDateFrom(LocalDate.now().minusDays(7))
                                .build(),
                        1
                ),
                Arguments.of(
                        ClosedRentalFilterRequest.builder()
                                .page(1)
                                .size(5)
                                .build(),
                        0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideClosedRentalFilterRequests")
    void shouldFindPageClosedRentalsWithFilterAndReturnPage(ClosedRentalFilterRequest request, int expectedContentSize) {
        // Given
        Rental rental = TestObjectUtils.createTestRental();
        Page<Rental> expectedPage = new Page<>(
                expectedContentSize > 0 ? List.of(rental) : List.of(),
                request.getPage(),
                request.getSize(),
                expectedContentSize
        );

        // When
        when(rentalRepository.findAllClosedWithFilter(request)).thenReturn(expectedPage);
        Page<Rental> actualPage = rentalService.findPageClosedRentalsWithFilter(request);

        // Then
        assertEquals(expectedContentSize, actualPage.getContent().size());
        assertEquals(request.getPage(), actualPage.getPageNumber());
        verify(rentalRepository).findAllClosedWithFilter(request);
    }

    @Test
    void shouldCreateRentalWhenBookIsAvailable() {
        // Given
        Book book = TestObjectUtils.createTestBook();
        book.setAvailableCount(1);
        User user = TestObjectUtils.createTestUser();
        LocalDate rentDate = LocalDate.now();
        Integer rentDuration = 7;
        Rental expectedRental = TestObjectUtils.createTestRental(user, book, rentDate, rentDuration);

        // When
        when(rentalRepository.save(any(Rental.class))).thenReturn(expectedRental);
        Rental actualRental = rentalService.create(book, user, rentDate, rentDuration);

        // Then
        assertThat(actualRental).isEqualTo(expectedRental);
        assertEquals(0, book.getAvailableCount());
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void shouldThrowOutOfStockExceptionWhenNoAvailableBooks() {
        // Given
        Book book = TestObjectUtils.createTestBook();
        book.setAvailableCount(0);
        User user = TestObjectUtils.createTestUser();

        // When
        // Then
        assertThrows(OutOfStockException.class,
                () -> rentalService.create(book, user, LocalDate.now(), 14));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenBookIsDeleted() {
        // Given
        Book book = TestObjectUtils.createTestBook();
        book.setDeletedAt(LocalDateTime.now());
        User user = TestObjectUtils.createTestUser();

        // When
        // Then
        assertThrows(IllegalStateException.class,
                () -> rentalService.create(book, user, LocalDate.now(), 7));
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void shouldUpdateAndReturnUpdatedRental() {
        // Given
        Rental expectedRental = TestObjectUtils.createTestRental();

        // When
        when(rentalRepository.update(any(Rental.class))).thenReturn(expectedRental);
        Rental actualRental = rentalService.update(expectedRental);

        // Then
        assertThat(actualRental).isEqualTo(expectedRental);
        verify(rentalRepository).update(expectedRental);
    }

    @Test
    void shouldCloseRentalAndUpdateBookCount() {
        // Given
        Rental rental = TestObjectUtils.createTestRental();
        Book book = TestObjectUtils.createTestBook();
        book.setAvailableCount(0);
        rental.setBook(book);

        // When
        rentalService.closeRental(rental);

        // Then
        assertEquals(LocalDate.now(), rental.getReturnDate());
        assertEquals(1, book.getAvailableCount());
    }

    private static Stream<Arguments> provideRentalFilterRequests() {
        return Stream.of(
                Arguments.of(
                        RentalFilterRequest.builder().build(),
                        1
                ),
                Arguments.of(
                        RentalFilterRequest.builder()
                                .isClosed(false)
                                .build(),
                        1
                ),
                Arguments.of(
                        RentalFilterRequest.builder()
                                .page(1)
                                .size(5)
                                .build(),
                        0
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideRentalFilterRequests")
    void shouldGetBookRentalsWithFilterAndReturnPage(RentalFilterRequest request, int expectedContentSize) {
        // Given
        Long bookId = 1L;
        Rental rental = TestObjectUtils.createTestRental();
        Page<Rental> expectedPage = new Page<>(
                expectedContentSize > 0 ? List.of(rental) : List.of(),
                request.getPage(),
                request.getSize(),
                expectedContentSize
        );

        // When
        when(rentalRepository.findByBookIdWithFilter(bookId, request)).thenReturn(expectedPage);
        Page<Rental> actualPage = rentalService.getBookRentals(bookId, request);

        // Then
        assertEquals(expectedContentSize, actualPage.getContent().size());
        verify(rentalRepository).findByBookIdWithFilter(bookId, request);
    }
}
