package org.example.service.domain;

import org.example.domain.Role;
import org.example.enums.UserRole;

public interface RoleService {
    Role findByName(UserRole name);
}
