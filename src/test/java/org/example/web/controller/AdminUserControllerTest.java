package org.example.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.mapping.UserMappingService;
import org.example.web.dto.role.RoleRequest;
import org.example.web.dto.user.request.UserSortRequest;
import org.example.web.dto.user.response.UserPageResponse;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {
    @Mock
    private UserMappingService userMappingService;

    @InjectMocks
    private AdminUserController adminUserController;

    @Test
    void shouldUpdateUserRolesAndReturnUpdatedUser() {
        // Given
        Long userId = 1L;
        RoleRequest request = new RoleRequest();
        UserResponse expectedResponse = new UserResponse();

        // When
        when(userMappingService.updateUserRoles(userId, request)).thenReturn(expectedResponse);
        ResponseEntity<UserResponse> actualResponse = adminUserController.updateUserRoles(userId, request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(userMappingService).updateUserRoles(userId, request);
    }

    @Test
    void shouldDeactivateUserAndReturnNoContent() {
        // Given
        Long userId = 1L;

        // When
        ResponseEntity<UserResponse> actualResponse = adminUserController.deactivateUser(userId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());
        assertNull(actualResponse.getBody());
        verify(userMappingService).deactivateUser(userId);
    }

    @Test
    void shouldActivateUserAndReturnNoContent() {
        // Given
        Long userId = 1L;

        // When
        ResponseEntity<Void> actualResponse = adminUserController.activateUser(userId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());
        assertNull(actualResponse.getBody());
        verify(userMappingService).activateUser(userId);
    }

    @Test
    void shouldGetAllUsersWithSorting() {
        // Given
        UserSortRequest request = new UserSortRequest();
        UserPageResponse expected = new UserPageResponse();

        // When
        when(userMappingService.getAllUsersWithSort(request)).thenReturn(expected);
        ResponseEntity<UserPageResponse> actualResponse = adminUserController.getAllUsersWithSorting(request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).getAllUsersWithSort(request);
    }

    @Test
    void shouldThrowExceptionWhenDeactivationFails() {
        // Given
        Long userId = 1L;

        // When
        doThrow(new EntityNotFoundException("User not found")).when(userMappingService).deactivateUser(userId);

        // Then
        assertThrows(EntityNotFoundException.class, () -> adminUserController.deactivateUser(userId));
    }

    @Test
    void shouldThrowExceptionWhenActivationFails() {
        // Given
        Long userId = 1L;

        // When
        doThrow(new EntityNotFoundException("User not found")).when(userMappingService).activateUser(userId);

        // Then
        assertThrows(EntityNotFoundException.class, () -> adminUserController.activateUser(userId));
    }
}
