package org.example.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.domain.RefreshToken;
import org.example.domain.User;
import org.example.repository.RefreshTokenRepository;
import org.example.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;


    @Override
    @Transactional
    public RefreshToken generateRefreshToken(User user) {
        Long tokensCount = refreshTokenRepository.countByUser(user);

        if (tokensCount >= securityProperties.getMaxRefreshTokensCount()) {
            refreshTokenRepository.deleteAllByUser(user);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(securityProperties.getRefreshExpiration()));
        user.addRefreshToken(refreshToken);

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid refresh token"));
    }
}
