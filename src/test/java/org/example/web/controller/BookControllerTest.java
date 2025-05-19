package org.example.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.mapping.BookMappingService;
import org.example.web.dto.book.request.BookCreateRequest;
import org.example.web.dto.book.request.BookFilterRequest;
import org.example.web.dto.book.request.BookUpdateRequest;
import org.example.web.dto.book.response.BookPageResponse;
import org.example.web.dto.book.response.BookResponse;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
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
public class BookControllerTest {

    @Mock
    private BookMappingService bookMappingService;

    @InjectMocks
    private BookController bookController;

    @Test
    void shouldGetBookByIdAndReturnResponse() {
        // Given
        Long id = 1L;
        BookResponse expected = new BookResponse();

        // when
        when(bookMappingService.getBookById(id)).thenReturn(expected);
        ResponseEntity<BookResponse> actualResponse = bookController.getBookById(id);

        // then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(bookMappingService).getBookById(id);
    }

    @Test
    void shouldGetBookRentalsAndReturnResponse() {
        // Given
        Long id = 1L;
        RentalFilterRequest request = new RentalFilterRequest();
        RentalPageResponse expected = new RentalPageResponse();

        // When
        when(bookMappingService.getBookRentals(id, request)).thenReturn(expected);
        ResponseEntity<RentalPageResponse> actualResponse = bookController.getBookRentals(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(bookMappingService).getBookRentals(id, request);
    }

    @Test
    void shouldGetAllBooksWithFilterAndReturnResponse() {
        // Given
        BookFilterRequest request = new BookFilterRequest();
        BookPageResponse expected = new BookPageResponse();

        // When
        when(bookMappingService.getAllBooksWithFilter(request)).thenReturn(expected);
        ResponseEntity<BookPageResponse> actualResponse = bookController.getAllBooks(request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(bookMappingService).getAllBooksWithFilter(request);
    }

    @Test
    void shouldCreateBookAndReturnCreatedResponse() {
        // Given
        BookCreateRequest request = new BookCreateRequest();
        BookResponse expected = new BookResponse();

        // When
        when(bookMappingService.createBook(request)).thenReturn(expected);
        ResponseEntity<BookResponse> actualResponse = bookController.createBook(request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(bookMappingService).createBook(request);
    }

    @Test
    void shouldUpdateBookAndReturnUpdatedResponse() {
        // Given
        Long id = 1L;
        BookUpdateRequest request = new BookUpdateRequest();
        BookResponse expected = new BookResponse();

        // When
        when(bookMappingService.updateBook(id, request)).thenReturn(expected);
        ResponseEntity<BookResponse> actualResponse = bookController.updateBook(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(bookMappingService).updateBook(id, request);
    }

    @Test
    void shouldDeleteBookAndReturnNoContent() {
        // Given
        Long id = 1L;

        // When
        doNothing().when(bookMappingService).deleteBook(id);
        ResponseEntity<Void> actualResponse = bookController.deleteBook(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());
        assertNull(actualResponse.getBody());
        verify(bookMappingService).deleteBook(id);
    }

    @Test
    void shouldThrowExceptionWhenGetBookByIdFails() {
        // Given
        Long id = 999L;

        // When
        when(bookMappingService.getBookById(id)).thenThrow(new EntityNotFoundException("Book not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> bookController.getBookById(id));
        verify(bookMappingService).getBookById(id);
    }

    @Test
    void shouldThrowExceptionWhenCreateBookFails() {
        // Given
        BookCreateRequest request = new BookCreateRequest();

        // When
        when(bookMappingService.createBook(request)).thenThrow(new RuntimeException("Create failed"));

        // Then
        assertThrows(RuntimeException.class, () -> bookController.createBook(request));
        verify(bookMappingService).createBook(request);
    }

    @Test
    void shouldThrowExceptionWhenUpdateBookFails() {
        // Given
        Long id = 999L;
        BookUpdateRequest request = new BookUpdateRequest();

        // When
        when(bookMappingService.updateBook(id, request)).thenThrow(new EntityNotFoundException("Update failed"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> bookController.updateBook(id, request));
        verify(bookMappingService).updateBook(id, request);
    }

    @Test
    void shouldThrowExceptionWhenDeleteBookFails() {
        // Given
        Long id = 999L;

        // When
        doThrow(new RuntimeException("Delete failed")).when(bookMappingService).deleteBook(id);

        // Then
        assertThrows(RuntimeException.class, () -> bookController.deleteBook(id));
        verify(bookMappingService).deleteBook(id);
    }
}
