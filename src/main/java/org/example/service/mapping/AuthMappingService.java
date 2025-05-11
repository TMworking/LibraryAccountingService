package org.example.service.mapping;

import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;

public interface AuthMappingService {
    AuthResponse login(UserLoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
