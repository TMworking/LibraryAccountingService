package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Role;
import org.example.enums.UserRole;
import org.example.repository.RoleRepository;
import org.example.service.domain.impl.RoleServiceImpl;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void shouldFindByNameAndReturnRoleWhenRoleExists() {
        // Given
        UserRole roleName = UserRole.ADMIN;
        Role expectedRole = TestObjectUtils.createTestRole(roleName);

        // When
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(expectedRole));
        Role actualRole = roleService.findByName(roleName);

        // Then
        assertThat(actualRole).isEqualTo(expectedRole);
        assertEquals(roleName, actualRole.getUserRole());
        verify(roleRepository).findByName(roleName);
    }

    @Test
    void shouldFindByNameAndThrowEntityNotFoundExceptionWhenRoleNotExists() {
        // Given
        UserRole roleName = UserRole.ADMIN;

        // When
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.findByName(roleName)
        );
        assertEquals("Role with name ADMIN not found", exception.getMessage());
        verify(roleRepository).findByName(roleName);
    }

    @Test
    void shouldFindAllRolesByIdsAndReturnRolesWhenRolesExist() {
        // Given
        List<Long> ids = List.of(1L, 2L);
        Role adminRole = TestObjectUtils.createTestRole(UserRole.ADMIN);
        Role userRole = TestObjectUtils.createTestRole(UserRole.USER);
        userRole.setId(2L);

        // When
        when(roleRepository.findAllByIds(ids)).thenReturn(List.of(adminRole, userRole));
        List<Role> result = roleService.findAllRolesByIds(ids);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(adminRole));
        assertTrue(result.contains(userRole));
        verify(roleRepository).findAllByIds(ids);
    }

    @Test
    void shouldFindAllRolesByIdsAndReturnEmptyListWhenRolesNotExist() {
        // Given
        List<Long> ids = List.of(999L);

        // When
        when(roleRepository.findAllByIds(ids)).thenReturn(List.of());
        List<Role> result = roleService.findAllRolesByIds(ids);

        // Then
        assertTrue(result.isEmpty());
        verify(roleRepository).findAllByIds(ids);
    }

    @Test
    void shouldFindAllRolesByIdsAndReturnPartialListWhenSomeRolesExist() {
        // Given
        List<Long> ids = List.of(1L, 999L);
        Role adminRole = TestObjectUtils.createTestRole(UserRole.ADMIN);

        // When
        when(roleRepository.findAllByIds(ids)).thenReturn(List.of(adminRole));
        List<Role> result = roleService.findAllRolesByIds(ids);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(adminRole));
        verify(roleRepository).findAllByIds(ids);
    }
}
