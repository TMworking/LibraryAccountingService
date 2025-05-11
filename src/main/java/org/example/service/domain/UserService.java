package org.example.service.domain;

import org.example.domain.Rental;
import org.example.domain.User;
import org.example.enums.RoleAction;
import org.example.model.Page;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.user.request.UserSortRequest;

import java.util.List;

public interface UserService {
    User findById(Long id);
    Page<User> findPageUsersWithSorting(UserSortRequest request);
    Page<Rental> getUserRentals(Long id, RentalFilterRequest filterRequest);
    User create(User user);
    User update(User user);
    void changeUserPassword(User user, String oldPassword, String newPassword);
    User updateUserRoles(Long userId, RoleAction roleAction, List<Long> roleIds);
}
