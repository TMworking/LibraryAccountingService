package org.example.service.domain;

import org.example.domain.User;
import org.example.enums.RoleAction;
import org.example.model.Page;
import org.example.web.dto.user.request.UserSortRequest;

import java.util.List;

public interface UserService {
    User findById(Long id);
    Page<User> findPageUsersWithSorting(UserSortRequest request);
    User create(User user);
    User update(User user);
    void changeUserPassword(User user, String oldPassword, String newPassword);
    User updateUserRoles(User user, RoleAction roleAction, List<Long> roleIds);
}
