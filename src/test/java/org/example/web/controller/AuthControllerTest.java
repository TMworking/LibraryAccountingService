package org.example.web.controller;

import org.example.service.mapping.UserMappingService;
import org.example.service.mapping.impl.AuthMappingServiceImpl;
import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.example.web.dto.user.response.UserRegisterResponse;
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
public class AuthControllerTest {

    @Mock
    private UserMappingService userMappingService;

    @Mock
    private AuthMappingServiceImpl authMappingService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldRegisterUserAndReturnCreatedResponse() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        UserRegisterResponse expected = new UserRegisterResponse();

        // When
        when(userMappingService.createUser(request)).thenReturn(expected);
        ResponseEntity<UserRegisterResponse> actualResponse = authController.register(request);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(userMappingService).createUser(request);
    }

    @Test
    void shouldLoginUserAndReturnAuthResponse() {
        // Given
        UserLoginRequest request = new UserLoginRequest();
        AuthResponse expected = new AuthResponse();

        // When
        when(authMappingService.login(request)).thenReturn(expected);
        ResponseEntity<AuthResponse> actualResponse = authController.login(request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authMappingService).login(request);
    }

    @Test
    void shouldRefreshTokenAndReturnAuthResponse() {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest();
        AuthResponse expected = new AuthResponse();

        // When
        when(authMappingService.refreshToken(request)).thenReturn(expected);
        ResponseEntity<AuthResponse> actualResponse = authController.refreshToken(request);

        // Then
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expected, actualResponse.getBody());
        verify(authMappingService).refreshToken(request);
    }

    @Test
    void shouldThrowExceptionWhenRegisterFails() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();

        // When
        when(userMappingService.createUser(request)).thenThrow(new RuntimeException("Registration failed"));

        // Then
        assertThrows(RuntimeException.class, () -> authController.register(request));
        verify(userMappingService).createUser(request);
    }

    @Test
    void shouldThrowExceptionWhenLoginFails() {
        // Given
        UserLoginRequest request = new UserLoginRequest();

        // When
        when(authMappingService.login(request)).thenThrow(new RuntimeException("Login failed"));

        // Then
        assertThrows(RuntimeException.class, () -> authController.login(request));
        verify(authMappingService).login(request);
    }

    @Test
    void shouldThrowExceptionWhenRefreshFails() {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest();

        // When
        when(authMappingService.refreshToken(request)).thenThrow(new RuntimeException("Refresh failed"));

        // Then
        assertThrows(RuntimeException.class, () -> authController.refreshToken(request));
        verify(authMappingService).refreshToken(request);
    }
}
