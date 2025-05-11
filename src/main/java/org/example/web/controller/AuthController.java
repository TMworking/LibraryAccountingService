package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.mapping.UserMappingService;
import org.example.service.mapping.impl.AuthMappingServiceImpl;
import org.example.web.dto.auth.AuthResponse;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.example.web.dto.user.response.UserRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserMappingService userMappingService;
    private final AuthMappingServiceImpl authMappingService;

    @PostMapping("/registration")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userMappingService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok().body(authMappingService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok().body(authMappingService.refreshToken(request));
    }
}
