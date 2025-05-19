package org.example.service.domain.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.enums.RoleAction;
import org.example.enums.UserRole;
import org.example.model.Page;
import org.example.repository.UserRepository;
import org.example.service.domain.RoleService;
import org.example.service.domain.UserService;
import org.example.web.dto.user.request.UserSortRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public User findById(Long id) {
        log.debug("Searching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new EntityNotFoundException(
                            MessageFormat.format("User with id {0} not found", id)
                    );
                });
        log.debug("Found user: ID = {}", id);
        return user;
    }

    @Override
    public Page<User> findPageUsersWithSorting(UserSortRequest request) {
        log.debug("Fetching users page with filter: {}", request);
        Page<User> page = userRepository.findAll(request);
        log.debug("Retrieved {} users (page {})", page.getContent().size(), page.getPageNumber());
        return page;
    }

    @Override
    @Transactional
    public User create(User user) {
        log.debug("Creating new user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.debug("Adding default USER role to user");
        user.addRole(roleService.findByName(UserRole.USER));
        User createdUser = userRepository.save(user);
        log.debug("User created successfully");
        return createdUser;
    }

    @Override
    @Transactional
    public User update(User user) {
        log.debug("Updating user: ID={}", user.getId());
        User updatedUser = userRepository.update(user);
        log.debug("User updated successfully: {}", updatedUser);
        return updatedUser;
    }

    @Override
    @Transactional
    public void changeUserPassword(User user, String oldPassword, String newPassword) {
        log.debug("Changing user password: ID={}", user.getId());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("Password is incorrect");
            throw new BadCredentialsException("Password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            log.error("New password must be different from current");
            throw new IllegalArgumentException("New password must be different from current");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        log.debug("User password updated successfully: ID={}", user.getId());
    }

    @Override
    @Transactional
    public User updateUserRoles(User user, RoleAction roleAction, List<Long> roleIds) {
        log.debug("Updating user roles: ID={}", user.getId());
        List<Role> roles = roleService.findAllRolesByIds(roleIds);
        if (roleAction.equals(RoleAction.ADD)) {
            roles.forEach(user::addRole);
        } else {
            roles.forEach(user::removeRole);
        }
        log.debug("User roles updated successfully: ID={}", user.getId());
        return user;
    }
}
