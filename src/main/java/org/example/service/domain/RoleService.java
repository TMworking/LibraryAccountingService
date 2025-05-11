package org.example.service.domain;

import org.example.domain.Role;
import org.example.enums.UserRole;

import java.util.List;

public interface RoleService {
    Role findByName(UserRole name);
    List<Role> findAllRolesByIds(List<Long> ids);
}
