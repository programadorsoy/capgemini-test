package com.capgemini.test.code.domain.service;

import com.capgemini.test.code.domain.model.Role;
import com.capgemini.test.code.domain.model.User;
import com.capgemini.test.code.domain.repository.UserRepository;
import com.capgemini.test.code.exception.DuplicateEmailException;
import com.capgemini.test.code.exception.InvalidDniException;
import com.capgemini.test.code.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test unitario de UserService con Mockito.
 * Cubre el contrato actual del cliente:
 *  - createUser: happy path, email duplicado, DNI inválido, asignación de sala 1, notificación.
 *  - getUserById: encontrado y no encontrado.
 *  - deleteUser: happy path y no encontrado.
 * El cliente SOLO pide duplicado por email (no DNI/teléfono).
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private DniValidationPort dniValidationPort;
    @Mock private NotificationPort notificationPort;

    @InjectMocks private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .name("pablo")
                .email("email@email.com")
                .phone("677998899")
                .dni("23454234W")
                .role(Role.ADMIN)
                .build();
    }

    // ---------- createUser ----------

    @Test
    @DisplayName("createUser: guarda, asigna sala 1 y notifica cuando todo es válido")
    void createUser_happyPath() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(dniValidationPort.isValidDni(anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser(validUser);

        assertThat(result).isNotNull();
        assertThat(result.getRoomId()).isEqualTo(1L);
        verify(userRepository).save(validUser);
        verify(notificationPort).notifyUserCreated(validUser);
        verify(userRepository, never()).existsByDni(anyString());
        verify(userRepository, never()).existsByPhone(anyString());
    }

    @Test
    @DisplayName("createUser: lanza DuplicateEmailException si el email ya existe")
    void createUser_duplicateEmail() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(validUser))
                .isInstanceOf(DuplicateEmailException.class);

        verify(userRepository, never()).save(any());
        verify(notificationPort, never()).notifyUserCreated(any());
    }

    @Test
    @DisplayName("createUser: lanza InvalidDniException si MockServer rechaza el DNI")
    void createUser_invalidDni() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(dniValidationPort.isValidDni(validUser.getDni())).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(validUser))
                .isInstanceOf(InvalidDniException.class);

        verify(userRepository, never()).save(any());
        verify(notificationPort, never()).notifyUserCreated(any());
    }

    // ---------- getUserById ----------

    @Test
    @DisplayName("getUserById: devuelve el usuario cuando existe")
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));
        assertThat(userService.getUserById(1L)).isEqualTo(validUser);
    }

    @Test
    @DisplayName("getUserById: lanza UserNotFoundException cuando no existe")
    void getUserById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ---------- deleteUser ----------

    @Test
    @DisplayName("deleteUser: elimina y notifica")
    void deleteUser_happyPath() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        verify(notificationPort).notifyUserDeleted(validUser);
    }

    @Test
    @DisplayName("deleteUser: lanza UserNotFoundException si no existe")
    void deleteUser_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).deleteById(any());
    }
}