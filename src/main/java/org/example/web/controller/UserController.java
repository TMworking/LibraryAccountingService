package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // TODO: accessControlService
    @GetMapping("/{id}")
    @PreAuthorize("@accessControlService.canViewUser(principal.id, #id)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserResponse response = new UserResponse();
        // TODO
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{id}/rentals")
    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<RentalPageResponse> getUserRentals(@PathVariable Long userId, @Valid RentalFilterRequest request) {
        RentalPageResponse response = new RentalPageResponse();
        // TODO
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = new UserResponse();
        // TODO
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<Void> updateUserPassword(@PathVariable("id") Long id, @Valid @RequestBody UserPasswordChangeRequest request) {
        // TODO
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("/{id}/passport")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<UserResponse> updateUserPassport(@PathVariable("id") Long id, @Valid @RequestBody UserPassportChangeRequest request) {
        UserResponse response = new UserResponse();
        // TODO
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}/email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<UserResponse> updateUserEmail(@PathVariable("id") Long id, @Valid @RequestBody UserEmailChangeRequest request) {
        UserResponse response = new UserResponse();
        // TODO
        return ResponseEntity.ok().body(response);
    }
    // TODO: user cant change his mail
}
