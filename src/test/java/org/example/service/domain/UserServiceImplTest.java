package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.enums.RoleAction;
import org.example.enums.UserRole;
import org.example.model.Page;
import org.example.model.SortOption;
import org.example.repository.UserRepository;
import org.example.service.domain.impl.UserServiceImpl;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.user.request.UserSortRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldFindByIdAndReturnUserWhenUserExists() {
        // Given
        User expectedUser = TestObjectUtils.createTestUser();

        // When
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.findById(1L);

        // Then
        assertThat(actualUser).isEqualTo(expectedUser);
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldFindByIdAndThrowEntityNotFoundExceptionWhenUserNotExists() {
        // When
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> userService.findById(999L));
        verify(userRepository).findById(999L);
    }

    private static Stream<Arguments> provideUserSortRequests() {
        return Stream.of(
                Arguments.of(
                        UserSortRequest.builder().build(),
                        1
                ),
                Arguments.of(
                        UserSortRequest.builder()
                                .sortOptionList(List.of(new SortOption("name", "desc")))
                                .build(),
                        1
                ),
                Arguments.of(
                        UserSortRequest.builder()
                                .page(1)
                                .size(5)
                                .build(),
                        0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserSortRequests")
    void shouldFindPageUsersWithSortingAndReturnPage(UserSortRequest request, int expectedContentSize
    ) {
        // Given
        User user = TestObjectUtils.createTestUser();
        Page<User> expectedPage = new Page<>(
                expectedContentSize > 0 ? List.of(user) : List.of(),
                request.getPage(),
                request.getSize(),
                expectedContentSize
        );

        // When
        when(userRepository.findAll(request)).thenReturn(expectedPage);
        Page<User> actualPage = userService.findPageUsersWithSorting(request);

        // Then
        assertEquals(expectedContentSize, actualPage.getContent().size());
        assertEquals(request.getPage(), actualPage.getPageNumber());
        verify(userRepository).findAll(request);
    }

    @Test
    void shouldCreateUserWithEncodedPasswordAndDefaultRole() {
        // Given
        User user = TestObjectUtils.createTestUser();
        Role userRole = TestObjectUtils.createTestRole(UserRole.USER);
        String encodedPassword = "encodedPassword";

        // When
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(roleService.findByName(UserRole.USER)).thenReturn(userRole);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        // Then
        assertEquals(encodedPassword, createdUser.getPassword());
        assertTrue(createdUser.getRoles().contains(userRole));
        verify(roleService).findByName(UserRole.USER);
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        User user = TestObjectUtils.createTestUser();

        // When
        when(userRepository.update(user)).thenReturn(user);
        User updatedUser = userService.update(user);

        // Then
        assertThat(updatedUser).isEqualTo(user);
        verify(userRepository).update(user);
    }

    @Test
    void shouldChangeUserPasswordWhenOldPasswordCorrect() {
        // Given
        User user = TestObjectUtils.createTestUser();
        String oldPassword = "oldPassword";
        user.setPassword(oldPassword);
        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";

        // When
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        userService.changeUserPassword(user, oldPassword, newPassword);

        // Then
        assertEquals(encodedNewPassword, user.getPassword());
        verify(passwordEncoder).encode(newPassword);
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenOldPasswordDoesNotMatch() {
        // Given
        User user = TestObjectUtils.createTestUser();
        String wrongPassword = "wrongPassword";
        String newPassword = "newPassword";

        // When
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        // Then
        assertThrows(BadCredentialsException.class,
                () -> userService.changeUserPassword(user, wrongPassword, newPassword));
        verify(passwordEncoder).matches(wrongPassword, user.getPassword());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNewPasswordSameAsCurrent() {
        // Given
        User user = TestObjectUtils.createTestUser();
        String oldPassword = "oldPassword";
        String samePassword = "samePassword";

        // When
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(samePassword, user.getPassword())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class,
                () -> userService.changeUserPassword(user, oldPassword, samePassword));
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldAddRolesToUserWhenRoleActionIsAdd() {
        // Given
        User user = TestObjectUtils.createTestUser();
        Role adminRole = TestObjectUtils.createTestRole(UserRole.ADMIN);
        List<Long> roleIds = List.of(1L);

        // When
        when(roleService.findAllRolesByIds(roleIds)).thenReturn(List.of(adminRole));
        User updatedUser = userService.updateUserRoles(user, RoleAction.ADD, roleIds);

        // Then
        assertTrue(updatedUser.getRoles().contains(adminRole));
        verify(roleService).findAllRolesByIds(roleIds);
    }

    @Test
    void shouldRemoveRolesFromUserWhenRoleActionIsRemove() {
        // Given
        User user = TestObjectUtils.createTestUser();
        Role adminRole = TestObjectUtils.createTestRole(UserRole.ADMIN);
        user.addRole(adminRole);
        List<Long> roleIds = List.of(1L);

        // When
        when(roleService.findAllRolesByIds(roleIds)).thenReturn(List.of(adminRole));
        User updatedUser = userService.updateUserRoles(user, RoleAction.REMOVE, roleIds);

        // Then
        assertFalse(updatedUser.getRoles().contains(adminRole));
        verify(roleService).findAllRolesByIds(roleIds);
    }
}
