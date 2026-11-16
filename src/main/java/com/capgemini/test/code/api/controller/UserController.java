package com.capgemini.test.code.api.controller;

import com.capgemini.test.code.api.dto.*;
import com.capgemini.test.code.domain.model.Role;
import com.capgemini.test.code.domain.model.User;
import com.capgemini.test.code.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * API REST de usuarios.
 * Endpoints exigidos por el cliente:
 *  - POST /users       → 201 { "id": ... }  (crea en sala 1)
 *  - GET  /users/{id}  → 200 UserResponse    (404 si no existe)
 * Endpoints extra (no pedidos, pero mantenidos por completitud):
 *  - PUT/PATCH/DELETE /users/{id}
 * Conversión "rol" (String, minúsculas) ↔ Role (enum, mayúsculas).
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        User saved = userService.createUser(toDomain(req));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location)
                .body(new CreateUserResponse(saved.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateUserRequest req) {
        User updated = userService.updateUser(id, toDomain(req));
        return ResponseEntity.ok(toResponse(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patch(@PathVariable Long id,
                                              @Valid @RequestBody PatchUserRequest req) {
        User partial = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .dni(req.getDni())
                .role(req.getRol() != null ? Role.valueOf(req.getRol().toUpperCase()) : null)
                .build();
        return ResponseEntity.ok(toResponse(userService.patchUser(id, partial)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // --- Mapeos DTO ↔ dominio ---

    private User toDomain(CreateUserRequest r) {
        return User.builder()
                .name(r.getName())
                .email(r.getEmail())
                .phone(r.getPhone())
                .dni(r.getDni())
                .role(Role.valueOf(r.getRol().toUpperCase()))
                .build();
    }

    private User toDomain(UpdateUserRequest r) {
        return User.builder()
                .name(r.getName())
                .email(r.getEmail())
                .phone(r.getPhone())
                .dni(r.getDni())
                .role(Role.valueOf(r.getRol().toUpperCase()))
                .build();
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .dni(u.getDni())
                .rol(u.getRole() != null ? u.getRole().name().toLowerCase() : null)
                .roomId(u.getRoomId())
                .build();
    }
}