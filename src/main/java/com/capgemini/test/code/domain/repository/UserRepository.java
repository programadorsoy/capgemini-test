package com.capgemini.test.code.domain.repository;

import com.capgemini.test.code.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByPhone(String phone);
    void deleteById(Long id);
}