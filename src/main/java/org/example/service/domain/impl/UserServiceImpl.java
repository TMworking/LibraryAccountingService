package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Rental;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.enums.RoleAction;
import org.example.enums.UserRole;
import org.example.exception.NotFoundException;
import org.example.model.Page;
import org.example.repository.RentalRepository;
import org.example.repository.UserRepository;
import org.example.service.domain.RoleService;
import org.example.service.domain.UserService;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.user.request.UserSortRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("User with id {0} not found", id)
                ));
    }

    @Override
    public Page<User> findPageUsersWithSorting(UserSortRequest request) {
        return userRepository.findAll(request);
    }

    @Override
    public Page<Rental> getUserRentals(Long id, RentalFilterRequest filterRequest) {
        return rentalRepository.findByUserIdWithFilter(id, filterRequest);
    }

    @Override
    @Transactional
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleService.findByName(UserRole.USER));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    @Transactional
    public void changeUserPassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public User updateUserRoles(Long userId, RoleAction roleAction, List<Long> roleIds) {
        User user = findById(userId);
        List<Role> roles = roleService.findAllRolesByIds(roleIds);
        if (roleAction.equals(RoleAction.ADD)) {
            roles.forEach(user::addRole);
        } else {
            roles.forEach(user::removeRole);
        }
        return user;
    }
}
