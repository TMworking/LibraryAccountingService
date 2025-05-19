package org.example.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;


    @Override
    @Transactional
    public RefreshToken generateRefreshToken(User user) {
        log.debug("Fetching user's current count of refresh tokens: ID={}", user.getId());
        Long tokensCount = refreshTokenRepository.countByUser(user);

        if (tokensCount >= securityProperties.getMaxRefreshTokensCount()) {
            log.debug("Deleting all old user refresh tokens: ID={}", user.getId());
            refreshTokenRepository.deleteAllByUser(user);
            log.debug("Successfully deleted all old refresh tokens");
        }

        log.debug("Generating new refresh token for user: ID={}", user.getId());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(securityProperties.getRefreshExpiration()));
        user.addRefreshToken(refreshToken);

        RefreshToken createdRefreshToken = refreshTokenRepository.save(refreshToken);
        log.debug("Successfully created new refresh token for user: ID={}", user.getId());

        return createdRefreshToken;
    }

    @Override
    public RefreshToken findByToken(String token) {
        log.debug("Searching refresh token by token");
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Refresh token not found");
                    return new EntityNotFoundException("Invalid refresh token");
                });
        log.debug("Found refresh token");
        return refreshToken;
    }
}
