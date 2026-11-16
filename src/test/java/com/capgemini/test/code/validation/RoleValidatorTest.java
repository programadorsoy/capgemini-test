package com.capgemini.test.code.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test unitario de RoleValidator.
 * Roles válidos: "admin" / "superadmin" (case-insensitive).
 * Cualquier otro valor → false (salvo null, que se deja pasar para que @NotBlank lo cubra).
 */
class RoleValidatorTest {

    private final RoleValidator validator = new RoleValidator();

    @Test
    @DisplayName("roles válidos → true")
    void validRole() {
        assertThat(validator.isValid("admin", null)).isTrue();
        assertThat(validator.isValid("superadmin", null)).isTrue();
        assertThat(validator.isValid("ADMIN", null)).isTrue();
        assertThat(validator.isValid("SUPERADMIN", null)).isTrue();
        assertThat(validator.isValid("SuperAdmin", null)).isTrue();
    }

    @Test
    @DisplayName("roles inválidos → false")
    void invalidRole() {
        assertThat(validator.isValid("user", null)).isFalse();
        assertThat(validator.isValid("root", null)).isFalse();
        assertThat(validator.isValid("ADMINISTRATOR", null)).isFalse();
        assertThat(validator.isValid("admin2", null)).isFalse();
        assertThat(validator.isValid("", null)).isFalse();
    }

    @Test
    @DisplayName("null → true (deja pasar; @NotBlank lo cubre)")
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}