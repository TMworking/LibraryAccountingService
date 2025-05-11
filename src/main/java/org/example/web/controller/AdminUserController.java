package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.mapping.UserMappingService;
import org.example.web.dto.role.RoleRequest;
import org.example.web.dto.user.request.UserSortRequest;
import org.example.web.dto.user.response.UserPageResponse;
import org.example.web.dto.user.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Admin")
public class AdminUserController {

    private final UserMappingService userMappingService;

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRoles(@PathVariable("id") Long id, @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok().body(userMappingService.updateUserRoles(id, request));
    }

    @GetMapping("/{id}/deactivation")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable("id") Long id) {
        userMappingService.deactivateUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}/activation")
    public ResponseEntity<Void> activateUser(@PathVariable("id") Long id) {
        userMappingService.activateUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<UserPageResponse> getAllUsersWithSorting(@Valid @RequestBody UserSortRequest request) {
        return ResponseEntity.ok().body(userMappingService.getAllUsersWithSort(request));
    }
}
