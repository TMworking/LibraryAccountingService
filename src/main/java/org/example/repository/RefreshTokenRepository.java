package org.example.repository;

import org.example.domain.RefreshToken;
import org.example.domain.User;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(RefreshToken refreshToken);
    RefreshToken update(RefreshToken refreshToken);
    void deleteAllByUser(User user);
    Long countByUser(User user);
}
