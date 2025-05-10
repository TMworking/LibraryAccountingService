package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.service.domain.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Long id) {
        return null;
    }
}
