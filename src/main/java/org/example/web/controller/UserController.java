package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.mapping.UserMappingService;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.user.request.UserEmailChangeRequest;
import org.example.web.dto.user.request.UserPassportChangeRequest;
import org.example.web.dto.user.request.UserPasswordChangeRequest;
import org.example.web.dto.user.request.UserUpdateRequest;
import org.example.web.dto.user.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User")
public class UserController {

    private final UserMappingService userMappingService;

    // TODO: accessControlService
    @GetMapping("/{id}")
    @PreAuthorize("@accessControlService.canViewUser(principal.id, #id)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userMappingService.getUserById(id));
    }

    @PostMapping("/{id}/rentals")
    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<RentalPageResponse> getUserRentals(@PathVariable("id") Long userId, @Valid @RequestBody RentalFilterRequest request) {
        return ResponseEntity.ok(userMappingService.getUserRentals(userId, request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok().body(userMappingService.updateUserInfo(id, request));
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<Void> updateUserPassword(@PathVariable("id") Long id, @Valid @RequestBody UserPasswordChangeRequest request) {
        userMappingService.updateUserPassword(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("/{id}/passport")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<UserResponse> updateUserPassport(@PathVariable("id") Long id, @Valid @RequestBody UserPassportChangeRequest request) {
        return ResponseEntity.ok().body(userMappingService.updateUserPassport(id, request));
    }

    @PatchMapping("/{id}/email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<UserResponse> updateUserEmail(@PathVariable("id") Long id, @Valid @RequestBody UserEmailChangeRequest request) {
        return ResponseEntity.ok().body(userMappingService.updateUserEmail(id, request));
    }
}
