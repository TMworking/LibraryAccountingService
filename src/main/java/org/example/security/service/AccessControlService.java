package org.example.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final UserRepository userRepository;

    public boolean canViewUser(Long principalId, Long targetId) {
        if (principalId.equals(targetId)) {
            return true;
        }

        User principal = userRepository.findById(principalId)
                .orElseThrow(() -> new EntityNotFoundException("Principal user not found"));

        User targetUser = userRepository.findById(targetId)
                .orElseThrow(() -> new EntityNotFoundException("Target user not found"));

        if (hasRole(principal, UserRole.ADMIN)) {
            return true;
        }

        if (hasRole(principal, UserRole.LIBRARIAN)) {
            return hasRole(targetUser, UserRole.USER);
        }

        return false;
    }

    private boolean hasRole(User user, UserRole userRole) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getUserRole() == userRole);
    }
}
