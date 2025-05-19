package org.example.security;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.RefreshToken;
import org.example.domain.User;
import org.example.repository.RefreshTokenRepository;
import org.example.security.service.RefreshTokenServiceImpl;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    void shouldGenerateNewRefreshTokenWhenTokensCountBelowMax() {
        // Given
        User user = TestObjectUtils.createTestUser();

        // When
        when(refreshTokenRepository.countByUser(user)).thenReturn(1L);
        when(securityProperties.getMaxRefreshTokensCount()).thenReturn(5);
        when(securityProperties.getRefreshExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RefreshToken result = refreshTokenService.generateRefreshToken(user);

        // Then
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertTrue(result.getExpiryDate().isAfter(Instant.now()));
        assertEquals(user, result.getUser());
        verify(refreshTokenRepository, never()).deleteAllByUser(any());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldDeleteOldTokensAndGenerateNewWhenTokensCountReachedMax() {
        // Given
        User user = TestObjectUtils.createTestUser();

        // When
        when(refreshTokenRepository.countByUser(user)).thenReturn(5L);
        when(securityProperties.getMaxRefreshTokensCount()).thenReturn(5);
        when(securityProperties.getRefreshExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RefreshToken actualToken = refreshTokenService.generateRefreshToken(user);

        // Then
        assertNotNull(actualToken);
        verify(refreshTokenRepository).deleteAllByUser(user);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldFindRefreshTokenByTokenWhenTokenExists() {
        // Given
        RefreshToken expectedToken = TestObjectUtils.createTestRefreshToken();

        // When
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(expectedToken));
        RefreshToken actualToken = refreshTokenService.findByToken(expectedToken.getToken());

        // Then
        assertNotNull(actualToken);
        assertEquals(expectedToken.getToken(), actualToken.getToken());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenTokenNotExists() {
        // Given

        // When
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> refreshTokenService.findByToken("invalid-token"));
    }
}
