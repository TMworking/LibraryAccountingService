package org.example.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.mapping.AuthorMappingService;
import org.example.web.dto.author.request.AuthorRequest;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.example.web.dto.author.response.AuthorPageResponse;
import org.example.web.dto.author.response.AuthorResponse;
import org.example.web.dto.book.request.AddBooksRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    @Mock
    private AuthorMappingService authorMappingService;

    @InjectMocks
    private AuthorController authorController;

    @Test
    void shouldGetAuthorByIdAndReturnResponse() {
        // Given
        Long id = 1L;
        AuthorResponse expected = new AuthorResponse();

        // When
        when(authorMappingService.getAuthorById(id)).thenReturn(expected);
        ResponseEntity<AuthorResponse> actualResponse = authorController.getAuthorById(id);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authorMappingService).getAuthorById(id);
    }

    @Test
    void shouldGetAllAuthorsWithSortAndReturnResponse() {
        // Given
        AuthorSortRequest request = new AuthorSortRequest();
        AuthorPageResponse expected = new AuthorPageResponse();

        // When
        when(authorMappingService.getAuthorsWithSort(request)).thenReturn(expected);
        ResponseEntity<AuthorPageResponse> actualResponse = authorController.getAllAuthors(request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authorMappingService).getAuthorsWithSort(request);
    }

    @Test
    void shouldCreateAuthorAndReturnCreatedResponse() {
        // Given
        AuthorRequest request = new AuthorRequest();
        AuthorResponse expected = new AuthorResponse();

        // When
        when(authorMappingService.createAuthor(request)).thenReturn(expected);
        ResponseEntity<AuthorResponse> actualResponse = authorController.createAuthor(request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authorMappingService).createAuthor(request);
    }

    @Test
    void shouldUpdateAuthorAndReturnUpdatedResponse() {
        // Given
        Long id = 1L;
        AuthorRequest request = new AuthorRequest();
        AuthorResponse expected = new AuthorResponse();

        // When
        when(authorMappingService.updateAuthor(id, request)).thenReturn(expected);
        ResponseEntity<AuthorResponse> actualResponse = authorController.updateAuthor(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authorMappingService).updateAuthor(id, request);
    }

    @Test
    void shouldAddBooksToAuthorAndReturnUpdatedResponse() {
        // Given
        Long id = 1L;
        AddBooksRequest request = new AddBooksRequest();
        AuthorResponse expected = new AuthorResponse();

        // When
        when(authorMappingService.addBooksToAuthor(id, request)).thenReturn(expected);
        ResponseEntity<AuthorResponse> actualResponse = authorController.addBooksToAuthor(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authorMappingService).addBooksToAuthor(id, request);
    }

    @Test
    void shouldThrowExceptionWhenGetAuthorByIdFails() {
        // Given
        Long id = 999L;

        // When
        when(authorMappingService.getAuthorById(id)).thenThrow(new EntityNotFoundException("Not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> authorController.getAuthorById(id));
        verify(authorMappingService).getAuthorById(id);
    }

    @Test
    void shouldThrowExceptionWhenCreateAuthorFails() {
        // Given
        AuthorRequest request = new AuthorRequest();

        // When
        when(authorMappingService.createAuthor(request)).thenThrow(new RuntimeException("Creation failed"));

        // Then
        assertThrows(RuntimeException.class, () -> authorController.createAuthor(request));
        verify(authorMappingService).createAuthor(request);
    }

    @Test
    void shouldThrowExceptionWhenUpdateAuthorFails() {
        // Given
        Long id = 999L;
        AuthorRequest request = new AuthorRequest();

        // When
        when(authorMappingService.updateAuthor(id, request)).thenThrow(new EntityNotFoundException("Update failed"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> authorController.updateAuthor(id, request));
        verify(authorMappingService).updateAuthor(id, request);
    }

    @Test
    void shouldThrowExceptionWhenAddBooksFails() {
        // Given
        Long id = 999L;
        AddBooksRequest request = new AddBooksRequest();

        // When
        when(authorMappingService.addBooksToAuthor(id, request)).thenThrow(new EntityNotFoundException("Add books failed"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> authorController.addBooksToAuthor(id, request));
        verify(authorMappingService).addBooksToAuthor(id, request);
    }
}
