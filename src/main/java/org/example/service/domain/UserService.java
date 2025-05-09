package org.example.service.domain;

import org.example.domain.User;

public interface UserService {
    User findById(Long id);
}
