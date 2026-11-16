package com.capgemini.test.code.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test unitario de DniValidator: comprueba SOLO el formato (8 dígitos + letra).
 * La validez semántica la decide MockServer (PATCH /check-dni), no este validador.
 * No necesita contexto Spring ni mocks.
 */
class DniValidatorTest {

    private final DniValidator validator = new DniValidator();

    @Test
    @DisplayName("formato válido → true")
    void validFormat() {
        assertThat(validator.isValid("12345678Z", null)).isTrue();
        assertThat(validator.isValid("23454234W", null)).isTrue(); // ejemplo del cliente
        assertThat(validator.isValid("99999999w", null)).isTrue(); // formato OK; MockServer lo rechazará
    }

    @Test
    @DisplayName("formato inválido → false")
    void invalidFormat() {
        assertThat(validator.isValid("1234567Z", null)).isFalse();   // 7 dígitos
        assertThat(validator.isValid("123456789Z", null)).isFalse(); // 9 dígitos
        assertThat(validator.isValid("12345678", null)).isFalse();   // sin letra
        assertThat(validator.isValid("ABCDEFGHZ", null)).isFalse();  // sin dígitos
        assertThat(validator.isValid("", null)).isFalse();           // vacío
        assertThat(validator.isValid("hola", null)).isFalse();       // texto
    }

    @Test
    @DisplayName("null → true (deja pasar; @NotBlank lo cubre)")
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}