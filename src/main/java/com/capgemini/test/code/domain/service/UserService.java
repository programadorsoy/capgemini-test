package com.capgemini.test.code.domain.service;

import com.capgemini.test.code.domain.model.User;
import com.capgemini.test.code.domain.repository.UserRepository;
import com.capgemini.test.code.exception.DuplicateEmailException;
import com.capgemini.test.code.exception.InvalidDniException;
import com.capgemini.test.code.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Lógica de negocio de usuarios.
 * createUser:
 *  - Duplicado: SOLO por email (el cliente no menciona DNI/teléfono como duplicados).
 *  - Valida el DNI contra el puerto (MockServer vía Feign).
 *  - Asigna la sala 1 (requisito: "guarda un usuario en la sala 1").
 *  - Persiste y notifica según rol (admin→email, superadmin→sms).
 * getUserById: lanza UserNotFoundException si no existe (→ 404).
 *
 * PUT/PATCH/DELETE se mantienen por completitud, aunque el cliente solo pide POST y GET.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DniValidationPort dniValidationPort;
    private final NotificationPort notificationPort;

    public UserService(UserRepository userRepository,
                       DniValidationPort dniValidationPort,
                       NotificationPort notificationPort) {
        this.userRepository = userRepository;
        this.dniValidationPort = dniValidationPort;
        this.notificationPort = notificationPort;
    }

    public User createUser(User user) {
        // 1) Duplicado por email → 409
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }
        // 2) Validación del DNI contra MockServer → 400 si inválido
        if (!dniValidationPort.isValidDni(user.getDni())) {
            throw new InvalidDniException(user.getDni());
        }
        // 3) Asignación fija a la sala 1
        user.setRoomId(1L);
        // 4) Persistencia + notificación por rol
        User saved = userRepository.save(user);
        notificationPort.notifyUserCreated(saved);
        return saved;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // --- PUT/PATCH/DELETE opcionales (no pedidos por el cliente) ---

    public User updateUser(Long id, User updated) {
        User existing = getUserById(id);

        if (!existing.getEmail().equals(updated.getEmail())
                && userRepository.existsByEmail(updated.getEmail())) {
            throw new DuplicateEmailException(updated.getEmail());
        }
        if (!dniValidationPort.isValidDni(updated.getDni())) {
            throw new InvalidDniException(updated.getDni());
        }

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setDni(updated.getDni());
        existing.setRole(updated.getRole());
        return userRepository.save(existing);
    }

    public User patchUser(Long id, User partial) {
        User existing = getUserById(id);

        if (partial.getName() != null) existing.setName(partial.getName());
        if (partial.getEmail() != null) {
            if (!existing.getEmail().equals(partial.getEmail())
                    && userRepository.existsByEmail(partial.getEmail())) {
                throw new DuplicateEmailException(partial.getEmail());
            }
            existing.setEmail(partial.getEmail());
        }
        if (partial.getPhone() != null) existing.setPhone(partial.getPhone());
        if (partial.getDni() != null) {
            if (!dniValidationPort.isValidDni(partial.getDni())) {
                throw new InvalidDniException(partial.getDni());
            }
            existing.setDni(partial.getDni());
        }
        if (partial.getRole() != null) existing.setRole(partial.getRole());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User existing = getUserById(id);
        userRepository.deleteById(id);
        notificationPort.notifyUserDeleted(existing);
    }
}