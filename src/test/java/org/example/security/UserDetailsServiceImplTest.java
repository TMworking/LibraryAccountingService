package org.example.security;

import org.example.domain.User;
import org.example.repository.UserRepository;
import org.example.security.service.UserDetailsServiceImpl;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsernameWhenUserExists() {
        // Given
        User expectedUser = TestObjectUtils.createTestUser();

        // When
        when(userRepository.findByEmail(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser));
        UserDetails actualUser = userDetailsService.loadUserByUsername(expectedUser.getEmail());

        // Then
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        verify(userRepository).findByEmail(expectedUser.getEmail());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotExists() {
        // Given
        User expectedUser = TestObjectUtils.createTestUser();

        // When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(expectedUser.getEmail())
        );
        verify(userRepository).findByEmail(expectedUser.getEmail());
    }
}
