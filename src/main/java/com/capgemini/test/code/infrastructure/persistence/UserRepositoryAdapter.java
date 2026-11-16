package com.capgemini.test.code.infrastructure.persistence;

import com.capgemini.test.code.domain.model.Role;
import com.capgemini.test.code.domain.model.User;
import com.capgemini.test.code.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementación del puerto UserRepository con Spring Data JPA.
 * - UserEntity.id es Integer (SERIAL en Postgres); User.id es Long → se convierte.
 * - roomId: Integer en entidad, Long en dominio → se convierte.
 * - Al actualizar, se conserva el id del dominio si viene informado.
 */
@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public User save(User user) {
        UserEntity saved = jpa.save(toEntity(user));
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpa.findById(id.intValue()).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) { return jpa.existsByEmail(email); }

    @Override
    public boolean existsByDni(String dni) { return jpa.existsByDni(dni); }

    @Override
    public boolean existsByPhone(String phone) { return jpa.existsByPhone(phone); }

    @Override
    public void deleteById(Long id) {
        if (id != null) jpa.deleteById(id.intValue());
    }

    private UserEntity toEntity(User u) {
        return UserEntity.builder()
                .id(u.getId() != null ? u.getId().intValue() : null)        // Long → Integer
                .name(u.getName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .dni(u.getDni())
                .role(u.getRole() != null ? u.getRole().name() : null)
                .roomId(u.getRoomId() != null ? u.getRoomId().intValue() : null) // Long → Integer
                .build();
    }

    private User toDomain(UserEntity e) {
        return User.builder()
                .id(e.getId() != null ? e.getId().longValue() : null)       // Integer → Long
                .name(e.getName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .dni(e.getDni())
                .role(e.getRole() != null ? Role.valueOf(e.getRole()) : null)
                .roomId(e.getRoomId() != null ? e.getRoomId().longValue() : null) // Integer → Long
                .build();
    }
}