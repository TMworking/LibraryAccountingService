package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.mappers.RentalMapper;
import org.example.mappers.UserMapper;
import org.example.service.domain.RentalService;
import org.example.service.domain.UserService;
import org.example.service.mapping.UserMappingService;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.role.RoleRequest;
import org.example.web.dto.user.request.UserEmailChangeRequest;
import org.example.web.dto.user.request.UserPassportChangeRequest;
import org.example.web.dto.user.request.UserPasswordChangeRequest;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.example.web.dto.user.request.UserSortRequest;
import org.example.web.dto.user.request.UserUpdateRequest;
import org.example.web.dto.user.response.UserPageResponse;
import org.example.web.dto.user.response.UserRegisterResponse;
import org.example.web.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMappingServiceImpl implements UserMappingService {

    private final UserMapper userMapper;
    private final UserService userService;
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @Override
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(userService.findById(id));
    }

    @Override
    public UserPageResponse getAllUsersWithSort(UserSortRequest request) {
        return userMapper.toPageResponse(userService.findPageUsersWithSorting(request));
    }

    @Override
    public RentalPageResponse getUserRentals(Long userId, RentalFilterRequest request) {
        return rentalMapper.toPageResponse(rentalService.getUserRentals(userId, request));
    }

    @Override
    public UserRegisterResponse createUser(UserRegisterRequest request) {
        return userMapper.toRegisterResponse(userService.create(userMapper.toUser(request)));
    }

    @Override
    public UserResponse updateUserInfo(Long id, UserUpdateRequest request) {
        User existingUser = userService.findById(id);
        userMapper.updateFromRequest(request, existingUser);
        User updatedUser = userService.update(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserPassport(Long id, UserPassportChangeRequest request) {
        User existingUser = userService.findById(id);
        existingUser.setPassport(request.getPassport());
        User updatedUser = userService.update(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserEmail(Long id, UserEmailChangeRequest request) {
        User existingUser = userService.findById(id);
        existingUser.setEmail(request.getEmail());
        User updatedUser = userService.update(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void updateUserPassword(Long id, UserPasswordChangeRequest request) {
        User user = userService.findById(id);
        userService.changeUserPassword(user, request.getOldPassword(), request.getNewPassword());
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userService.findById(id);
        user.setEnabled(false);
        userService.update(user);
    }

    @Override
    public void activateUser(Long id) {
        User user = userService.findById(id);
        user.setEnabled(true);
        userService.update(user);
    }

    @Override
    public UserResponse updateUserRoles(Long id, RoleRequest request) {
        User user = userService.findById(id);
        return userMapper.toResponse(userService.updateUserRoles(user, request.getRoleAction(), request.getRoleIds()));
    }
}
