package org.example.service.domain.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Role;
import org.example.enums.UserRole;
import org.example.repository.RoleRepository;
import org.example.service.domain.RoleService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(UserRole name) {
        log.debug("Searching role by name: {}", name);
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Role with name {0} not found", name)
                ));
        log.debug("Found role: {}", role);
        return role;
    }

    @Override
    public List<Role> findAllRolesByIds(List<Long> ids) {
        log.debug("Fetching multiple roles by IDs: {}", ids);
        List<Role> roles = roleRepository.findAllByIds(ids);
        log.debug("Found {} roles out of requested {}", roles.size(), ids.size());
        return roles;
    }
}
