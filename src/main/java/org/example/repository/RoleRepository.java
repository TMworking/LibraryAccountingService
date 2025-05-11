package org.example.repository;

import org.example.domain.Role;
import org.example.enums.UserRole;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(UserRole name);
}
