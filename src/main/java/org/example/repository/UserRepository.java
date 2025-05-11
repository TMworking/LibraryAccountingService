package org.example.repository;

import org.example.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    User save(User user);
    User update(User user);
}
