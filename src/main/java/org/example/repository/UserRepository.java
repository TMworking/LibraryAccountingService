package org.example.repository;

import org.example.domain.User;
import org.example.model.Page;
import org.example.web.dto.user.request.UserSortRequest;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Page<User> findAll(UserSortRequest request);
    User save(User user);
    User update(User user);
}
