package org.example.service.mapping;

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

public interface UserMappingService {
    UserResponse getUserById(Long id);
    UserPageResponse getAllUsersWithSort(UserSortRequest request);
    RentalPageResponse getUserRentals(Long userId, RentalFilterRequest request);
    UserRegisterResponse createUser(UserRegisterRequest request);
    UserResponse updateUserInfo(Long id, UserUpdateRequest request);
    UserResponse updateUserPassport(Long id, UserPassportChangeRequest request);
    UserResponse updateUserEmail(Long id, UserEmailChangeRequest request);
    UserResponse updateUserRoles(Long id, RoleRequest request);
    void updateUserPassword(Long id, UserPasswordChangeRequest request);
    void deactivateUser(Long id);
    void activateUser(Long id);
}
