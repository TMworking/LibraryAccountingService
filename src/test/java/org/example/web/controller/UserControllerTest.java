package org.example.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.mapping.UserMappingService;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.user.request.UserEmailChangeRequest;
import org.example.web.dto.user.request.UserPassportChangeRequest;
import org.example.web.dto.user.request.UserPasswordChangeRequest;
import org.example.web.dto.user.request.UserUpdateRequest;
import org.example.web.dto.user.response.UserResponse;
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
public class UserControllerTest {

    @Mock
    private UserMappingService userMappingService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldGetUserByIdAndReturnResponse() {
        // Given
        Long id = 1L;
        UserResponse expected = new UserResponse();

        // When
        when(userMappingService.getUserById(id)).thenReturn(expected);
        ResponseEntity<UserResponse> actualResponse = userController.getUserById(id);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).getUserById(id);
    }

    @Test
    void shouldGetUserRentalsAndReturnResponse() {
        // Given
        Long id = 1L;
        RentalFilterRequest request = new RentalFilterRequest();
        RentalPageResponse expected = new RentalPageResponse();

        // When
        when(userMappingService.getUserRentals(id, request)).thenReturn(expected);
        ResponseEntity<RentalPageResponse> actualResponse = userController.getUserRentals(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).getUserRentals(id, request);
    }

    @Test
    void shouldUpdateUserInfoAndReturnResponse() {
        // Given
        Long id = 1L;
        UserUpdateRequest request = new UserUpdateRequest();
        UserResponse expected = new UserResponse();

        // When
        when(userMappingService.updateUserInfo(id, request)).thenReturn(expected);
        ResponseEntity<UserResponse> actualResponse = userController.updateUserInfo(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).updateUserInfo(id, request);
    }

    @Test
    void shouldUpdateUserPasswordAndReturnNoContent() {
        // Given
        Long id = 1L;
        UserPasswordChangeRequest request = new UserPasswordChangeRequest();

        // When
        doNothing().when(userMappingService).updateUserPassword(id, request);
        ResponseEntity<Void> actualResponse = userController.updateUserPassword(id, request);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());
        assertNull(actualResponse.getBody());
        verify(userMappingService).updateUserPassword(id, request);
    }

    @Test
    void shouldUpdateUserPassportAndReturnResponse() {
        // Given
        Long id = 1L;
        UserPassportChangeRequest request = new UserPassportChangeRequest();
        UserResponse expected = new UserResponse();

        // When
        when(userMappingService.updateUserPassport(id, request)).thenReturn(expected);
        ResponseEntity<UserResponse> actualResponse = userController.updateUserPassport(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).updateUserPassport(id, request);
    }

    @Test
    void shouldUpdateUserEmailAndReturnResponse() {
        // Given
        Long id = 1L;
        UserEmailChangeRequest request = new UserEmailChangeRequest();
        UserResponse expected = new UserResponse();

        // When
        when(userMappingService.updateUserEmail(id, request)).thenReturn(expected);
        ResponseEntity<UserResponse> actualResponse = userController.updateUserEmail(id, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).updateUserEmail(id, request);
    }

    @Test
    void shouldThrowExceptionWhenGetUserByIdFails() {
        // Given
        Long id = 999L;

        // When
        when(userMappingService.getUserById(id)).thenThrow(new EntityNotFoundException("User not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> userController.getUserById(id));
        verify(userMappingService).getUserById(id);
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserInfoFails() {
        // given
        Long id = 999L;
        UserUpdateRequest request = new UserUpdateRequest();

        // when
        when(userMappingService.updateUserInfo(id, request)).thenThrow(new EntityNotFoundException("User not found"));

        // then
        assertThrows(EntityNotFoundException.class, () -> userController.updateUserInfo(id, request));
        verify(userMappingService).updateUserInfo(id, request);
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserPasswordFails() {
        // Given
        Long id = 999L;
        UserPasswordChangeRequest request = new UserPasswordChangeRequest();

        // When
        doThrow(new RuntimeException("Password update failed")).when(userMappingService).updateUserPassword(id, request);

        // Then
        assertThrows(RuntimeException.class, () -> userController.updateUserPassword(id, request));
        verify(userMappingService).updateUserPassword(id, request);
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserPassportFails() {
        // Given
        Long id = 999L;
        UserPassportChangeRequest request = new UserPassportChangeRequest();

        // When
        when(userMappingService.updateUserPassport(id, request)).thenThrow(new EntityNotFoundException("User not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> userController.updateUserPassport(id, request));
        verify(userMappingService).updateUserPassport(id, request);
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserEmailFails() {
        // Given
        Long id = 999L;
        UserEmailChangeRequest request = new UserEmailChangeRequest();

        // When
        when(userMappingService.updateUserEmail(id, request)).thenThrow(new EntityNotFoundException("User not found"));

        // Then
        assertThrows(EntityNotFoundException.class, () -> userController.updateUserEmail(id, request));
        verify(userMappingService).updateUserEmail(id, request);
    }
}
