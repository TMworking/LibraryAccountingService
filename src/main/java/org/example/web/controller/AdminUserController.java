package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web.dto.role.RoleRequest;
import org.example.web.dto.user.request.UserSortRequest;
import org.example.web.dto.user.response.UserPageResponse;
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
// TODO: only for ADMIN role
public class AdminUserController {

    @PatchMapping("/{id}/role/addendum")
    public ResponseEntity<Void> addRoleToUser(@PathVariable("id") Long id, @Valid @RequestBody RoleRequest request) {
        // TODO
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // TODO: collapse add and remove
    @PatchMapping("/{id}/role/removal")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable("id") Long id, @Valid @RequestBody RoleRequest request) {
        // TODO
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}/deactivation")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") Long id) {
        // TODO
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}/activation")
    public ResponseEntity<Void> activateUser(@PathVariable("id") Long id) {
        // TODO
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<UserPageResponse> getAllUsersWithSorting(@Valid @RequestBody UserSortRequest request) {
        UserPageResponse response = new UserPageResponse();
        // TODO
        return ResponseEntity.ok().body(response);
    }
}
