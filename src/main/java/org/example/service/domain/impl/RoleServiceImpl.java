package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Role;
import org.example.enums.UserRole;
import org.example.exception.NotFoundException;
import org.example.repository.RoleRepository;
import org.example.service.domain.RoleService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(UserRole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                MessageFormat.format("Role with name {0} not found", name)
        ));
    }
}
