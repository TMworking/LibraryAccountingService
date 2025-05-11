package org.example.security.service;

import org.example.domain.RefreshToken;
import org.example.domain.User;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken(User user);
    RefreshToken findByToken(String token);
}
