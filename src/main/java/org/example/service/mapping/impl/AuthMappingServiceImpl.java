package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.RefreshToken;
import org.example.domain.User;
import org.example.security.JwtTokenProvider;
import org.example.security.service.RefreshTokenService;
import org.example.security.service.UserDetailsServiceImpl;
import org.example.service.mapping.AuthMappingService;
import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMappingServiceImpl implements AuthMappingService {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public AuthResponse login(UserLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) auth.getPrincipal();
        String refreshToken = refreshTokenService.generateRefreshToken(user).getToken();
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        return new AuthResponse(refreshToken, accessToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenService.findByToken(request.getRefreshToken());

        User user = (User) userDetailsService.loadUserByUsername(
                storedToken.getUser().getUsername());

        String refreshToken = refreshTokenService.generateRefreshToken(user).getToken();
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        return new AuthResponse(refreshToken, accessToken);
    }
}
