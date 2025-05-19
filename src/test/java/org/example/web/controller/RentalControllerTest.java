package org.example.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.mapping.OverdueRentalMappingService;
import org.example.service.mapping.RentalMappingService;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.example.web.dto.rental.request.RentalCreateRequest;
import org.example.web.dto.rental.request.RentalProlongationRequest;
import org.example.web.dto.rental.response.OverdueRentalPageResponse;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.rental.response.RentalResponse;
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
public class RentalControllerTest {
    @Mock
    private RentalMappingService rentalMappingService;

    @Mock
    private OverdueRentalMappingService overdueRentalMappingService;

    @InjectMocks
    private RentalController rentalController;

    @Test
    void shouldCreateRentalAndReturnCreatedResponse() {
        // Given
        RentalCreateRequest request = new RentalCreateRequest();
        RentalResponse expected = new RentalResponse();

        // When
        when(rentalMappingService.createRental(request)).thenReturn(expected);
        ResponseEntity<RentalResponse> actualResponse = rentalController.createRental(request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(rentalMappingService).createRental(request);
    }

    @Test
    void shouldCloseRentalAndReturnNoContent() {
        // Given
        Long id = 1L;

        // When
        doNothing().when(rentalMappingService).closeRental(id);
        ResponseEntity<Void> actualResponse = rentalController.closeRental(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());
        assertNull(actualResponse.getBody());
        verify(rentalMappingService).closeRental(id);
    }

    @Test
    void shouldProlongRentalAndReturnUpdatedResponse() {
        // Given
        Long id = 1L;
        RentalProlongationRequest request = new RentalProlongationRequest();
        RentalResponse expected = new RentalResponse();

        // When
        when(rentalMappingService.updateRental(id, request)).thenReturn(expected);
        ResponseEntity<RentalResponse> actualResponse = rentalController.prolongRental(id, request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(rentalMappingService).updateRental(id, request);
    }

    @Test
    void shouldGetAllOverdueRentalsAndReturnResponse() {
        // Given
        OverdueRentalFilterRequest request = new OverdueRentalFilterRequest();
        OverdueRentalPageResponse expected = new OverdueRentalPageResponse();

        // When
        when(overdueRentalMappingService.getAllOverdueRentals(request)).thenReturn(expected);
        ResponseEntity<OverdueRentalPageResponse> actualResponse = rentalController.getAllOverdueRentals(request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(overdueRentalMappingService).getAllOverdueRentals(request);
    }

    @Test
    void shouldGetAllClosedRentalsAndReturnResponse() {
        // Given
        ClosedRentalFilterRequest request = new ClosedRentalFilterRequest();
        RentalPageResponse expected = new RentalPageResponse();

        // When
        when(rentalMappingService.getAllClosedRentalsWithFilter(request)).thenReturn(expected);
        ResponseEntity<RentalPageResponse> actualResponse = rentalController.getAllClosedRentals(request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(rentalMappingService).getAllClosedRentalsWithFilter(request);
    }

    @Test
    void shouldThrowExceptionWhenGetRentalByIdFails() {
        // Given
        Long id = 999L;

        // When
        when(rentalMappingService.getRentalById(id)).thenThrow(new EntityNotFoundException("Rental not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> rentalController.getRentalById(id));
        verify(rentalMappingService).getRentalById(id);
    }

    @Test
    void shouldThrowExceptionWhenCreateRentalFails() {
        // Given
        RentalCreateRequest request = new RentalCreateRequest();

        // When
        when(rentalMappingService.createRental(request)).thenThrow(new RuntimeException("Create failed"));

        // Then
        assertThrows(RuntimeException.class, () -> rentalController.createRental(request));
        verify(rentalMappingService).createRental(request);
    }

    @Test
    void shouldThrowExceptionWhenProlongRentalFails() {
        // Given
        Long id = 999L;
        RentalProlongationRequest request = new RentalProlongationRequest();

        // When
        when(rentalMappingService.updateRental(id, request)).thenThrow(new EntityNotFoundException("Rental not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> rentalController.prolongRental(id, request));
        verify(rentalMappingService).updateRental(id, request);
    }

    @Test
    void shouldThrowExceptionWhenCloseRentalFails() {
        // Given
        Long id = 999L;

        // When
        doThrow(new EntityNotFoundException("Rental not found")).when(rentalMappingService).closeRental(id);

        // Then
        assertThrows(EntityNotFoundException.class, () -> rentalController.closeRental(id));
        verify(rentalMappingService).closeRental(id);
    }
}
