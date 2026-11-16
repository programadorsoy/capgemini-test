package com.capgemini.test.code.infrastructure.persistence;

import com.capgemini.test.code.domain.model.Role;
import com.capgemini.test.code.domain.model.User;
import com.capgemini.test.code.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(UserRepositoryAdapter.class)
class UserRepositoryAdapterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private static final String EMAIL = "existente@test.com";
    private static final String DNI   = "11111111H";
    private static final String PHONE = "600000001";

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();

        User user = User.builder()
                .name("Existente")
                .email(EMAIL)
                .phone(PHONE)
                .dni(DNI)
                .role(Role.ADMIN)
                .roomId(1L)
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("save: persiste un usuario y permite recuperarlo por id")
    void save_shouldPersistAndFind() {
        User user = User.builder()
                .name("Ana")
                .email("ana@test.com")
                .phone("600111222")
                .dni("22222222J")
                .role(Role.ADMIN)
                .roomId(1L)
                .build();

        User saved = userRepository.save(user);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("ana@test.com");
        assertThat(found.get().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("existsByEmail: true cuando existe, false cuando no")
    void existsByEmail() {
        assertThat(userRepository.existsByEmail(EMAIL)).isTrue();
        assertThat(userRepository.existsByEmail("no-existe@test.com")).isFalse();
    }

    @Test
    @DisplayName("existsByDni: true cuando existe, false cuando no")
    void existsByDni() {
        assertThat(userRepository.existsByDni(DNI)).isTrue();
        assertThat(userRepository.existsByDni("99999999Z")).isFalse();
    }

    @Test
    @DisplayName("existsByPhone: true cuando existe, false cuando no")
    void existsByPhone() {
        assertThat(userRepository.existsByPhone(PHONE)).isTrue();
        assertThat(userRepository.existsByPhone("699999999")).isFalse();
    }

    @Test
    @DisplayName("findById: devuelve vacío cuando no existe")
    void findById_shouldReturnEmpty_whenNotFound() {
        assertThat(userRepository.findById(9999L)).isEmpty();
    }

    @Test
    @DisplayName("deleteById: elimina el usuario persistido")
    void deleteById_shouldRemoveUser() {
        UserEntity existing = userJpaRepository.findAll().get(0);
        Long id = existing.getId().longValue();

        userRepository.deleteById(id);

        assertThat(userJpaRepository.findById(existing.getId())).isEmpty();
        assertThat(userRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("save: actualiza un usuario existente (mismo id)")
    void save_shouldUpdateExisting() {
        UserEntity existing = userJpaRepository.findAll().get(0);
        Long id = existing.getId().longValue();

        User updated = User.builder()
                .id(id)                 // ← Opción A: el id va informado
                .name("Modificado")
                .email(EMAIL)
                .phone(PHONE)
                .dni(DNI)
                .role(Role.SUPERADMIN)
                .roomId(1L)
                .build();

        userRepository.save(updated);

        UserEntity reloaded = userJpaRepository.findById(existing.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Modificado");
        assertThat(reloaded.getRole()).isEqualTo("SUPERADMIN");
    }
}