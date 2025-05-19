package org.example.service.domain;

import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.repository.OverdueRentalsViewRepository;
import org.example.service.domain.impl.OverdueRentalsViewServiceImpl;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OverdueRentalsViewServiceImplTest {

    @Mock
    private OverdueRentalsViewRepository overdueRentalsViewRepository;

    @InjectMocks
    private OverdueRentalsViewServiceImpl overdueRentalsViewService;

    private static Stream<Arguments> provideFilterRequests() {
        return Stream.of(
                Arguments.of(
                        OverdueRentalFilterRequest.builder().build(),
                        1
                ),
                Arguments.of(
                        OverdueRentalFilterRequest.builder()
                                .userId(1L)
                                .build(),
                        1
                ),
                Arguments.of(
                        OverdueRentalFilterRequest.builder()
                                .bookId(1L)
                                .build(),
                        1
                ),
                Arguments.of(
                        OverdueRentalFilterRequest.builder()
                                .page(1)
                                .size(5)
                                .build(),
                        0
                ),
                Arguments.of(
                        OverdueRentalFilterRequest.builder()
                                .userId(999L)
                                .bookId(999L)
                                .build(),
                        0
                )
        );
    }

    @Test
    void shouldRefreshViewSuccessfully() {
        // Given
        // When
        overdueRentalsViewService.refreshView();

        // Then
        verify(overdueRentalsViewRepository).refreshOverdueRentalsView();
    }

    @ParameterizedTest
    @MethodSource("provideFilterRequests")
    void shouldGetOverdueRentalsWithFilterAndReturnPage(OverdueRentalFilterRequest request, int expectedContentSize) {
        // Given
        OverdueRentalView overdueRental = TestObjectUtils.createTestOverdueRentalView();
        Page<OverdueRentalView> expectedPage = new Page<>(
                expectedContentSize > 0 ? List.of(overdueRental) : List.of(),
                request.getPage(),
                request.getSize(),
                expectedContentSize
        );

        // When
        when(overdueRentalsViewRepository.findAllWithFilter(request)).thenReturn(expectedPage);
        Page<OverdueRentalView> actualPage = overdueRentalsViewService.getOverdueRentalsWithFilter(request);

        // Then
        assertEquals(actualPage.getContent().size(), expectedContentSize);
        assertEquals(actualPage.getPageNumber(), request.getPage());
        verify(overdueRentalsViewRepository).findAllWithFilter(request);
    }
}
